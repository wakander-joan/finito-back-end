package com.management.finito.assinatura.config;

import com.management.finito.handler.APIException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP do Asaas (https://docs.asaas.com). Autentica pelo header access_token.
 * Sandbox por padrão; em produção defina ASAAS_BASE_URL=https://api.asaas.com/v3 e a ASAAS_API_KEY.
 */
@Component
@Log4j2
public class AsaasClient {
    private final RestTemplate rest = new RestTemplate();

    @Value("${asaas.base-url:https://sandbox.asaas.com/api/v3}")
    private String baseUrl;

    @Value("${asaas.api-key:}")
    private String apiKey;

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("access_token", apiKey);
        return h;
    }

    /** Cria (ou reaproveita) um cliente no Asaas. Retorna o id (cus_xxx). */
    public String criarCliente(String nome, String email, String cpfCnpj) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", nome);
        body.put("email", email);
        body.put("cpfCnpj", cpfCnpj);
        Map<?, ?> resp = post("/customers", body);
        return (String) resp.get("id");
    }

    /**
     * Cria a assinatura mensal. billingType UNDEFINED = o cliente escolhe Pix/Boleto/Cartão
     * na página de fatura. Retorna o id da assinatura (sub_xxx).
     */
    public String criarAssinatura(String customerId, BigDecimal valor, String descricao, String successUrl) {
        Map<String, Object> body = new HashMap<>();
        body.put("customer", customerId);
        body.put("billingType", "UNDEFINED");
        body.put("value", valor);
        body.put("nextDueDate", LocalDate.now().toString());
        body.put("cycle", "MONTHLY");
        body.put("description", descricao);
        if (successUrl != null && !successUrl.isBlank()) {
            Map<String, Object> callback = new HashMap<>();
            callback.put("successUrl", successUrl);
            callback.put("autoRedirect", true);
            body.put("callback", callback);
        }
        Map<?, ?> resp = post("/subscriptions", body);
        return (String) resp.get("id");
    }

    /** URL da página de fatura da 1ª cobrança da assinatura (onde o cliente paga). */
    @SuppressWarnings("unchecked")
    public String primeiraFaturaUrl(String subscriptionId) {
        Map<?, ?> resp = get("/subscriptions/" + subscriptionId + "/payments");
        Object data = resp.get("data");
        if (data instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Map<?, ?> primeira) {
            Object url = primeira.get("invoiceUrl");
            return url != null ? url.toString() : null;
        }
        return null;
    }

    private Map<?, ?> post(String path, Map<String, Object> body) {
        garantiaChave();
        try {
            ResponseEntity<Map> r = rest.exchange(baseUrl + path, HttpMethod.POST,
                    new HttpEntity<>(body, headers()), Map.class);
            return r.getBody() != null ? r.getBody() : Map.of();
        } catch (RestClientException e) {
            log.error("Asaas POST {} falhou: {}", path, e.getMessage());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Falha ao falar com o provedor de pagamento.");
        }
    }

    private Map<?, ?> get(String path) {
        garantiaChave();
        try {
            ResponseEntity<Map> r = rest.exchange(baseUrl + path, HttpMethod.GET,
                    new HttpEntity<>(headers()), Map.class);
            return r.getBody() != null ? r.getBody() : Map.of();
        } catch (RestClientException e) {
            log.error("Asaas GET {} falhou: {}", path, e.getMessage());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Falha ao falar com o provedor de pagamento.");
        }
    }

    private void garantiaChave() {
        if (apiKey == null || apiKey.isBlank()) {
            throw APIException.build(HttpStatus.SERVICE_UNAVAILABLE,
                    "Pagamento indisponível: a chave do Asaas não está configurada.");
        }
    }
}
