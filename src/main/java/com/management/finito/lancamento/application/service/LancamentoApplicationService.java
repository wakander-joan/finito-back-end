package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
}
