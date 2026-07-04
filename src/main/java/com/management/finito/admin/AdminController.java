package com.management.finito.admin;

import com.management.finito.assinatura.application.repository.AssinaturaRepository;
import com.management.finito.assinatura.domain.Assinatura;
import com.management.finito.handler.APIException;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Admin API padronizada consumida pelo painel central (Ordersys Admin).
 * PÚBLICA no filtro do JWT, mas protegida pelo header X-Admin-Token == admin.token.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final PessoaRepository pessoaRepository;
    private final AssinaturaRepository assinaturaRepository;

    @Value("${admin.token:}")
    private String adminToken;

    private void autoriza(String token) {
        if (adminToken == null || adminToken.isBlank() || !adminToken.equals(token)) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Admin token inválido.");
        }
    }

    /** Métricas do site para o painel. */
    @GetMapping("/metrics")
    public Map<String, Object> metrics(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        autoriza(token);
        long usuarios = pessoaRepository.buscaTodasPessoas().size();
        long premium = assinaturaRepository.contaPremiumAtivas();
        return Map.of(
                "site", "Finito",
                "temPremium", true,
                "usuarios", usuarios,
                "premium", premium
        );
    }

    /** Concede Premium MANUAL a um usuário (sem cobrança), pelo e-mail. */
    @PostMapping("/premium")
    public Map<String, Object> tornaPremium(@RequestHeader(value = "X-Admin-Token", required = false) String token,
                                            @RequestBody Map<String, String> body) {
        autoriza(token);
        String email = body != null ? body.get("email") : null;
        if (email == null || email.isBlank()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Informe o email.");
        }
        Pessoa pessoa = pessoaRepository.buscaEmail(email.trim());
        if (pessoa == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + email);
        }
        Assinatura assinatura = assinaturaRepository.buscaPorUsuario(pessoa.getIdPessoa())
                .orElseGet(() -> new Assinatura(pessoa.getIdPessoa()));
        assinatura.setPlano("PREMIUM_MANUAL");
        assinatura.ativar(LocalDate.now().plusYears(100));
        assinaturaRepository.salva(assinatura);
        log.info("[admin] Premium MANUAL concedido a {}", email);
        return Map.of("ok", true, "email", email, "premium", true);
    }
}
