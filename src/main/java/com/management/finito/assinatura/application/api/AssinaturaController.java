package com.management.finito.assinatura.application.api;

import com.management.finito.assinatura.application.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assinatura")
@RequiredArgsConstructor
@Log4j2
public class AssinaturaController {
    private final AssinaturaService assinaturaService;

    /** Inicia o checkout do Premium e devolve a URL da fatura hospedada do Asaas. */
    @PostMapping("/checkout")
    public CheckoutResponse checkout(@RequestBody CheckoutRequest request) {
        log.info("[start] AssinaturaController - checkout");
        CheckoutResponse resp = assinaturaService.checkout(request);
        log.info("[finish] AssinaturaController - checkout");
        return resp;
    }

    /** Estado do premium do usuário logado (para o front decidir a UI). */
    @GetMapping("/me")
    public AssinaturaResponse me() {
        return assinaturaService.statusDoUsuarioLogado();
    }
}
