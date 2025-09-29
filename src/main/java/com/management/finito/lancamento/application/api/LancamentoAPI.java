package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/lancamento")
public interface LancamentoAPI {

    @PostMapping("/cadastraLancamento/{mes}/{ano}")
    @ResponseStatus(code = HttpStatus.CREATED)
    LancamentoResponse cadastraLancamento (@Valid @RequestBody LancamentoRequest LancamentoRequest, @PathVariable MesDoLancamento mes, @PathVariable int ano);

    @PostMapping("/cadastraLancamentoEmLote/{idMeta}")
    @ResponseStatus(code = HttpStatus.CREATED)
    void cadastraLancamentoEmLote (@RequestBody List<@Valid LancamentoEmLoteRequest> lancamentosEmLoteRequest, @PathVariable int idMeta);

@GetMapping("/buscaLancamento/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    LancamentoDetalhadoResponse buscaLancamento (@PathVariable UUID idLancamento);

    @GetMapping("/buscaLancamentosPorMesEAno/{mes}/{ano}")
    @ResponseStatus(code = HttpStatus.OK)
    List<LancamentoDetalhadoResponse> buscaLancamentosPorMesEAno(@PathVariable MesDoLancamento mes, @PathVariable int ano);

    @DeleteMapping("/deletaLancamento/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    void deletaLancamento(@PathVariable UUID idLancamento);

    @DeleteMapping("/deletaAllLancamentoRecorrente/{idRecorrencia}")
    @ResponseStatus(code = HttpStatus.OK)
    void deletaAllLancamentoRecorrente (@PathVariable int idRecorrencia);

    @PatchMapping(value = "/edita/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    void editaLancamento (@PathVariable UUID idLancamento, @Valid @RequestBody LancamentoAlteracaoRequest lancamentoAlteracaoRequest);

    @PatchMapping(value = "/statusPendente/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    void mudaStatusParaPendente (@PathVariable UUID idLancamento);

    @PatchMapping(value = "/statusPago/{idLancamento}")
    @ResponseStatus(code = HttpStatus.OK)
    void mudaStatusParaPago (@PathVariable UUID idLancamento);

    @PostMapping("/replicaLancamentos")
    @ResponseStatus(code = HttpStatus.CREATED)
    void replicaLancamentos(@Valid @RequestBody ReplicaLancamentosRequest replicaLancamentosRequest);
}
