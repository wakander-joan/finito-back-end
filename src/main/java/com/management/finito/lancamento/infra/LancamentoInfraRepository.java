package com.management.finito.lancamento.infra;

import com.management.finito.handler.APIException;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Lançamento não encontrado"));
        log.info("[finish] LancamentoInfraRepository - buscaLancamento");
        return lancamento;
    }

    @Override
    public List<Lancamento> buscaLancamentosPorMesEAno(MesDoLancamento mes, UUID idPessoa, int ano) {
        log.info("[start] LancamentoInfraRepository - buscaTodosLancamentoPorMes");
        List<Lancamento> listaDeLancamentos = lancamentoJPARepository.findAllByMesDoLancamentoAndIdPessoaAndAno(mes.getId(),idPessoa,ano);
        log.info("[finish] LancamentoInfraRepository - buscaTodosLancamentoPorMes");
        return listaDeLancamentos;
    }

    @Override
    public void deletaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoInfraRepository - deletaLancamento");
        lancamentoJPARepository.deleteById(idLancamento);
        log.info("[finish] LancamentoInfraRepository - deletaLancamento");
    }

    @Override
    public void deletaTodosLancamento(UUID idPessoa) {
        log.info("[start] LancamentoInfraRepository - deletaTodosLancamento");
        lancamentoJPARepository.deleteAllByIdPessoa(idPessoa);
        log.info("[finish] LancamentoInfraRepository - deletaTodosLancamento");
    }

    @Override
    public List<Lancamento> findByDataVencimento(LocalDate hoje) {
        log.info("[start] LancamentoInfraRepository - findByDataVencimento");
        List<Lancamento> lancamentos = lancamentoJPARepository.findAllByDataVencimentoAndTipo(hoje, 2);
        log.info("[finish] LancamentoInfraRepository - findByDataVencimento");
        return lancamentos;
    }

    @Override
    public void savaTodosLancamentos(List<Lancamento> novosLancamentos) {
        log.info("[start] LancamentoInfraRepository - savaTodosLancamentos");
        lancamentoJPARepository.saveAll(novosLancamentos);
        log.info("[finish] LancamentoInfraRepository - savaTodosLancamentos");
    }

    @Override
    public void deleteAllLancamentosMeta(int id) {
        log.info("[start] LancamentoInfraRepository - deleteAllLancamentosMeta");
        List<Lancamento> lancamentos = lancamentoJPARepository.findAllByIdMeta(id);
        for (Lancamento lancamento : lancamentos) {
            lancamentoJPARepository.deleteById(lancamento.getIdLancamento());
        }
        log.info("[finish] LancamentoInfraRepository - deleteAllLancamentosMeta");
    }

    @Override
    public List<Lancamento> buscaLancamentoIdRecorrencia(int idRecorrencia) {
        log.info("[start] LancamentoInfraRepository - buscaLancamentoIdRecorrencia");
        List<Lancamento> lancamentos = lancamentoJPARepository.findAllByIdRecorrencia(idRecorrencia);
        log.info("[finish] LancamentoInfraRepository - buscaLancamentoIdRecorrencia");
        return lancamentos;
    }

    @Override
    public void deleteAllLancamentosRecorrencia(List<Lancamento> lancamentos) {
        log.info("[start] LancamentoInfraRepository - deleteAllLancamentosRecorrencia");
        lancamentoJPARepository.deleteAll(lancamentos);
        log.info("[finish] LancamentoInfraRepository - deleteAllLancamentosRecorrencia");
    }

}
