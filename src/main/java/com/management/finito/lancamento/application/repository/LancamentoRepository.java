package com.management.finito.lancamento.application.repository;

import com.management.finito.lancamento.domain.Lancamento;

import java.util.UUID;

public interface LancamentoRepository {
    Lancamento salva(Lancamento lancamento);
    Lancamento buscaLancamento(UUID idLancamento);
}