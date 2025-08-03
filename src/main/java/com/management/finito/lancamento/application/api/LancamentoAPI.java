package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lancamento")
public interface LancamentoAPI {

    @PostMapping("/cadastraLancamento")
    @ResponseStatus(code = HttpStatus.CREATED)
    LancamentoResponse cadastraLancamento (@Valid @RequestBody LancamentoRequest LancamentoRequest);

    @GetMapping("/buscaLancamento/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    LancamentoDetalhadoResponse buscaLancamento (@PathVariable UUID idLancamento);

    @GetMapping("/buscaTodosLancamentoPorMes/{mes}")
    @ResponseStatus(code = HttpStatus.OK)
    List<LancamentoDetalhadoResponse> buscaTodosLancamentoPorMes(@PathVariable MesDoLancamento mes);

    @DeleteMapping("/deletaLancamento/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    void deletaLancamento(@PathVariable UUID idLancamento);
}
