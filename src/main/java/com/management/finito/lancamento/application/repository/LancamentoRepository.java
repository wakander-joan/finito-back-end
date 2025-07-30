package com.management.finito.lancamento.application.repository;

import com.management.finito.lancamento.domain.Lancamento;

public interface LancamentoRepository {
    Lancamento salva(Lancamento lancamento);
}