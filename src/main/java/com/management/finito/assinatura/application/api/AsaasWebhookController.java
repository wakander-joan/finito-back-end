package com.management.finito.assinatura.application.api;

import com.management.finito.assinatura.application.service.AssinaturaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Recebe os eventos do Asaas (pagamento confirmado/recebido/vencido/estornado).
 * Endpoint PÚBLICO (fora do JWT) — a autenticidade é verificada pelo header
 * asaas-access-token, comparado com asaas.webhook-token.
 */
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
@Log4j2
public class AsaasWebhookController {
    private final AssinaturaService assinaturaService;

    @Value("${asaas.webhook-token:}")
    private String webhookToken;

    @SuppressWarnings("unchecked")
    @PostMapping("/asaas")
    public ResponseEntity<Void> receber(@RequestHeader(value = "asaas-access-token", required = false) String token,
                                        @RequestBody Map<String, Object> body) {
        if (webhookToken != null && !webhookToken.isBlank() && !webhookToken.equals(token)) {
            log.warn("Webhook Asaas REJEITADO: token inválido.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String event = body.get("event") != null ? body.get("event").toString() : null;
        Object payment = body.get("payment");
        log.info("[start] Webhook Asaas | event={}", event);
        assinaturaService.processarWebhook(event, payment instanceof Map ? (Map<String, Object>) payment : null);
        log.info("[finish] Webhook Asaas");
        return ResponseEntity.ok().build();
    }
}
