package com.management.finito.lancamento.application.repository;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;

import java.util.List;
import java.util.UUID;

public interface LancamentoRepository {
    Lancamento salva(Lancamento lancamento);
    Lancamento buscaLancamento(UUID idLancamento);
    List<Lancamento> buscaTodosLancamentoPorMes(MesDoLancamento mes);
}