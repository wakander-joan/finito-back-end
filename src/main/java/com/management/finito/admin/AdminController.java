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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final AdminService adminService;
    private final AdminTelemetryService telemetry;

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
                "premium", premium,
                "acessosHoje", telemetry.acessosHoje()
        );
    }

    /** Série de acessos (logins) dos últimos dias, para o gráfico do painel. */
    @GetMapping("/acessos")
    public List<AcessoDiario> acessos(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        autoriza(token);
        return telemetry.ultimosAcessos(14);
    }

    /** Últimos erros da API, para o painel. */
    @GetMapping("/errors")
    public List<ErroLog> errors(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        autoriza(token);
        return telemetry.ultimosErros();
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

    /** Lista os usuários (nome, e-mail, se é premium e se foi manual). */
    @GetMapping("/usuarios")
    public List<Map<String, Object>> usuarios(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        autoriza(token);
        Map<UUID, Assinatura> porUsuario = new HashMap<>();
        for (Assinatura a : assinaturaRepository.todas()) porUsuario.put(a.getIdUsuario(), a);
        return pessoaRepository.buscaTodasPessoas().stream().map(p -> {
            Assinatura a = porUsuario.get(p.getIdPessoa());
            boolean premium = a != null && a.isPremium();
            boolean manual = premium && "PREMIUM_MANUAL".equals(a.getPlano());
            Map<String, Object> m = new HashMap<>();
            m.put("nome", p.getNomePessoa());
            m.put("email", p.getEmail());
            m.put("premium", premium);
            m.put("manual", manual);
            m.put("plano", premium ? a.getPlano() : null);
            return m;
        }).collect(Collectors.toList());
    }

    /** Remove o Premium concedido MANUALMENTE (não mexe em assinatura paga do Asaas). */
    @PostMapping("/premium/remover")
    public Map<String, Object> removePremium(@RequestHeader(value = "X-Admin-Token", required = false) String token,
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
        Assinatura a = assinaturaRepository.buscaPorUsuario(pessoa.getIdPessoa()).orElse(null);
        if (a == null || !a.isPremium()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Este usuário não é Premium.");
        }
        if (!"PREMIUM_MANUAL".equals(a.getPlano())) {
            throw APIException.build(HttpStatus.CONFLICT,
                    "Este Premium é uma assinatura paga (Asaas). Cancele pelo Asaas, não aqui.");
        }
        a.cancelar();
        a.setVigenteAte(LocalDate.now().minusDays(1));
        assinaturaRepository.salva(a);
        log.info("[admin] Premium MANUAL removido de {}", email);
        return Map.of("ok", true, "email", email, "premium", false);
    }

    /** APAGA o usuário e TODOS os dados dele (lançamentos, metas, assinatura). Irreversível. */
    @PostMapping("/usuarios/apagar")
    public Map<String, Object> apagaUsuario(@RequestHeader(value = "X-Admin-Token", required = false) String token,
                                            @RequestBody Map<String, String> body) {
        autoriza(token);
        String email = body != null ? body.get("email") : null;
        if (email == null || email.isBlank()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Informe o email.");
        }
        adminService.apagaUsuarioCompleto(email);
        return Map.of("ok", true, "email", email, "apagado", true);
    }
}
