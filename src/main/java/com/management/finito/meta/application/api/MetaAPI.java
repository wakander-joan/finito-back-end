package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.StatusEtapa;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/meta")
public interface MetaAPI {
    @PostMapping("/cadastraMeta")
    @ResponseStatus(code = HttpStatus.CREATED)
    MetaResponse cadastraMeta (@Valid @RequestBody MetaRequest metatoRequest);

    @GetMapping("/buscaMetas")
    @ResponseStatus(code = HttpStatus.OK)
    List<MetaDetalhadaResponse> buscaMetas();

    @DeleteMapping("/deletaMeta/{idMeta}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deletaMeta(@PathVariable UUID idMeta);

    @PatchMapping("/alteraStatusEtapa/{idEtapa}/{status}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void alteraStatusEtapa(@PathVariable UUID idEtapa, @PathVariable String status);
}
