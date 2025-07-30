package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;

public interface LancamentoService {
    LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest);
}
