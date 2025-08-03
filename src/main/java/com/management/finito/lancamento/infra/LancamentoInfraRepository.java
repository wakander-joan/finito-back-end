package com.management.finito.lancamento.infra;

import com.management.finito.handler.APIException;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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

    @Override
    public Lancamento buscaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoInfraRepository - buscaLancamento");
        Lancamento lancamento = lancamentoJPARepository.findById(idLancamento)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        log.info("[finish] LancamentoInfraRepository - buscaLancamento");
        return lancamento;
    }

    @Override
    public List<Lancamento> buscaTodosLancamentoPorMes(MesDoLancamento mes) {
        log.info("[start] LancamentoInfraRepository - buscaTodosLancamentoPorMes");
        List<Lancamento> listaDeLancamentos = lancamentoJPARepository.findAllByMesDoLancamento(mes.getId());
        log.info("[finish] LancamentoInfraRepository - buscaTodosLancamentoPorMes");
        return listaDeLancamentos;
    }
}
