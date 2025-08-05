package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.application.service.LancamentoService;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Log4j2
public class LancamentoController implements LancamentoAPI {
    private final LancamentoService lancamentoService;

    @Override
    public LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest, MesDoLancamento mes, int ano) {
        log.info("[start] LancamentoController - cadastraLancamento");
        LancamentoResponse response = lancamentoService.cadastraLancamento(lancamentoRequest, mes, ano);
        log.info("[finish] LancamentoController - cadastraLancamento");
        return response;
    }

    @Override
    public LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoController - buscaLancamento");
        LancamentoDetalhadoResponse lancamento = lancamentoService.buscaLancamento(idLancamento);
        log.info("[finish] LancamentoController - buscaLancamento");
        return lancamento;
    }

    @Override
    public List<LancamentoDetalhadoResponse> buscaLancamentosPorMesEAno(MesDoLancamento mes, int ano) {
        log.info("[start] LancamentoController - buscaTodosLancamentoPorMes");
        List<LancamentoDetalhadoResponse> listaDeLancamentos = lancamentoService.buscaLancamentosPorMesEAno(mes, ano);
        log.info("[finish] LancamentoController - buscaTodosLancamentoPorMes");
        return listaDeLancamentos;
    }

    @Override
    public void deletaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoController - deletaLancamento");
        lancamentoService.deletaLancamento(idLancamento);
        log.info("[finish] LancamentoController - deletaLancamento");
    }

    @Override
    public void editaLancamento(UUID idLancamento, LancamentoAlteracaoRequest lancamentoAlteracaoRequest) {
        log.info("[start] LancamentoController - editaLancamento");
        lancamentoService.editaLancamento(idLancamento, lancamentoAlteracaoRequest);
        log.info("[finish] LancamentoController - editaLancamento");
    }

    @Override
    public void mudaStatusParaPendente(UUID idLancamento) {
        log.info("[start] LancamentoController - mudaStatusParaPendente");
        lancamentoService.mudaStatusParaPendente(idLancamento);
        log.info("[finish] LancamentoController - mudaStatusParaPendente");
    }

    @Override
    public void mudaStatusParaPago(UUID idLancamento) {
        log.info("[start] LancamentoController - mudaStatusParaPago");
        lancamentoService.mudaStatusParaPago(idLancamento);
        log.info("[finish] LancamentoController - mudaStatusParaPago");
    }
}
