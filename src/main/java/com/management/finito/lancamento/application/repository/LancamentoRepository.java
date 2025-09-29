package com.management.finito.lancamento.application.repository;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LancamentoRepository {
    Lancamento salva(Lancamento lancamento);
    Lancamento buscaLancamento(UUID idLancamento);
    List<Lancamento> buscaLancamentosPorMesEAno(MesDoLancamento mes, UUID idPessoa, int ano);
    void deletaLancamento(UUID idLancamento);
    void deletaTodosLancamento(UUID idPessoa);
    List<Lancamento> findByDataVencimento(LocalDate hoje);
    void savaTodosLancamentos(List<Lancamento> novosLancamentos);
    void deleteAllLancamentosMeta(int id);
    List<Lancamento> buscaLancamentoIdRecorrencia(int idRecorrencia);
    void deleteAllLancamentosRecorrencia(List<Lancamento> lancamentos);
}