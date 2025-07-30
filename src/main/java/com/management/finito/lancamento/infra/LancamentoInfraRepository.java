package com.management.finito.lancamento.infra;

import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
@RequiredArgsConstructor
public class LancamentoInfraRepository implements LancamentoRepository {
    private final LancamentoJPARepository lancamentoJPARepository;

    @Override
    public Lancamento salva(Lancamento lancamento) {
        log.info("[start] LancamentoInfraRepository - salva");
        Lancamento lancamentoCriado =lancamentoJPARepository.save(lancamento);
        log.info("[finish] LancamentoInfraRepository - salva");
        return lancamentoCriado;
    }
}
