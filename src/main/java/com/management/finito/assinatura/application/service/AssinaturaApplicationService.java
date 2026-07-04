package com.management.finito.assinatura.application.service;

import com.management.finito.assinatura.application.api.AssinaturaResponse;
import com.management.finito.assinatura.application.api.CheckoutRequest;
import com.management.finito.assinatura.application.api.CheckoutResponse;
import com.management.finito.assinatura.application.repository.AssinaturaRepository;
import com.management.finito.assinatura.config.AsaasClient;
import com.management.finito.assinatura.domain.Assinatura;
import com.management.finito.handler.APIException;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AssinaturaApplicationService implements AssinaturaService {
    private final AssinaturaRepository repository;
    private final AsaasClient asaas;

    @Value("${premium.valor:9.90}")
    private BigDecimal valorPremium;

    private Pessoa usuarioLogado() {
        return (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {
        log.info("[start] AssinaturaApplicationService - checkout");
        Pessoa usuario = usuarioLogado();
        Assinatura assinatura = repository.buscaPorUsuario(usuario.getIdPessoa())
                .orElseGet(() -> new Assinatura(usuario.getIdPessoa()));

        if (assinatura.isPremium()) {
            throw APIException.build(HttpStatus.CONFLICT, "Você já é Premium. 👑");
        }

        String cpfCnpj = apenasDigitos(request.getCpfCnpj());
        if (cpfCnpj.length() != 11 && cpfCnpj.length() != 14) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Informe um CPF ou CNPJ válido.");
        }

        String customerId = assinatura.getAsaasCustomerId();
        if (customerId == null || customerId.isBlank()) {
            customerId = asaas.criarCliente(usuario.getNomePessoa(), usuario.getEmail(), cpfCnpj);
        }

        String subscriptionId = asaas.criarAssinatura(customerId, valorPremium, "Finito Premium", request.getRetornoUrl());
        String faturaUrl = asaas.primeiraFaturaUrl(subscriptionId);

        assinatura.iniciarCheckout(customerId, subscriptionId, cpfCnpj);
        repository.salva(assinatura);

        if (faturaUrl == null || faturaUrl.isBlank()) {
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Não foi possível gerar a fatura. Tente novamente.");
        }
        log.info("[finish] AssinaturaApplicationService - checkout | sub={}", subscriptionId);
        return new CheckoutResponse(faturaUrl);
    }

    @Override
    public AssinaturaResponse statusDoUsuarioLogado() {
        Pessoa usuario = usuarioLogado();
        Assinatura assinatura = repository.buscaPorUsuario(usuario.getIdPessoa()).orElse(null);
        boolean premium = assinatura != null && assinatura.isPremium();
        return new AssinaturaResponse(premium, assinatura, valorPremium);
    }

    @Override
    public void processarWebhook(String event, Map<String, Object> payment) {
        if (event == null || payment == null) return;
        String subscriptionId = str(payment.get("subscription"));
        String customerId = str(payment.get("customer"));

        Assinatura assinatura = null;
        if (subscriptionId != null) assinatura = repository.buscaPorAssinaturaAsaas(subscriptionId).orElse(null);
        if (assinatura == null && customerId != null) assinatura = repository.buscaPorCustomerAsaas(customerId).orElse(null);
        if (assinatura == null) {
            log.warn("Webhook Asaas sem assinatura correspondente (event={}, sub={}, cust={})", event, subscriptionId, customerId);
            return;
        }

        switch (event) {
            case "PAYMENT_CONFIRMED", "PAYMENT_RECEIVED", "PAYMENT_RECEIVED_IN_CASH" -> {
                LocalDate vencimento = parseData(str(payment.get("dueDate")));
                // vigência = vencimento + 1 mês + 3 dias de tolerância (determinístico -> webhook idempotente)
                LocalDate ate = (vencimento != null ? vencimento : LocalDate.now()).plusMonths(1).plusDays(3);
                assinatura.ativar(ate);
                log.info("Premium ATIVO até {} (sub={})", ate, subscriptionId);
            }
            case "PAYMENT_OVERDUE" -> assinatura.marcarAtrasada();
            case "PAYMENT_REFUNDED", "PAYMENT_DELETED", "SUBSCRIPTION_DELETED", "PAYMENT_CHARGEBACK_REQUESTED" ->
                    assinatura.cancelar();
            default -> log.info("Webhook Asaas ignorado: {}", event);
        }
        repository.salva(assinatura);
    }

    private static String apenasDigitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }

    private static String str(Object o) {
        return o == null ? null : o.toString();
    }

    private static LocalDate parseData(String iso) {
        if (iso == null || iso.isBlank()) return null;
        try {
            return LocalDate.parse(iso.substring(0, 10));
        } catch (DateTimeParseException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
