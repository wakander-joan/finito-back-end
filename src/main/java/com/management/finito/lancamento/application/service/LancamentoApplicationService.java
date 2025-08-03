package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoDetalhadoResponse;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Service
public class LancamentoApplicationService implements LancamentoService {
    private final LancamentoRepository lancamentoRepository;

    @Override
    public LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest) {
        log.info("[start] LancamentoApplicationService - cadastraLancamento");
        Lancamento lancamentoCriado = lancamentoRepository.salva(new Lancamento(lancamentoRequest));
        log.info("[finish] LancamentoApplicationService - cadastraLancamento");
        return new LancamentoResponse(lancamentoCriado);
    }

    @Override
    public LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - buscaLancamento");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        log.info("[finish] LancamentoApplicationService - buscaLancamento");
        return new LancamentoDetalhadoResponse(lancamento);
    }

    @Override
    public List<LancamentoDetalhadoResponse> buscaTodosLancamentoPorMes(MesDoLancamento mes) {
        log.info("[start] LancamentoApplicationService - buscaTodosLancamentoPorMes");
        List<Lancamento> lancamentos = lancamentoRepository.buscaTodosLancamentoPorMes(mes);
        log.info("[finish] LancamentoApplicationService - buscaTodosLancamentoPorMes");
        return LancamentoDetalhadoResponse.converte(lancamentos);
    }
}
