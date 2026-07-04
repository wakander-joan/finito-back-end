package com.management.finito.feedback;

import com.management.finito.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

/** Recebe feedback dos usuários. PÚBLICO (não exige login). */
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Log4j2
public class FeedbackController {
    private final FeedbackJPARepository repo;

    private static final Set<String> TIPOS = Set.of("RECLAMACAO", "SUGESTAO", "CONTATO");

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> enviar(@RequestBody FeedbackRequest r) {
        if (r == null || r.getMensagem() == null || r.getMensagem().isBlank()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Escreva uma mensagem.");
        }
        String tipo = r.getTipo() == null ? "" : r.getTipo().trim().toUpperCase();
        Feedback f = new Feedback();
        f.setTipo(TIPOS.contains(tipo) ? tipo : "CONTATO");
        f.setNome(r.getNome());
        f.setEmail(r.getEmail());
        f.setMensagem(r.getMensagem().length() > 4000 ? r.getMensagem().substring(0, 4000) : r.getMensagem());
        f.setQuando(Instant.now());
        f.setLido(false);
        repo.save(f);
        log.info("[feedback] {} recebido de {}", f.getTipo(), f.getEmail());
        return Map.of("ok", true);
    }
}
