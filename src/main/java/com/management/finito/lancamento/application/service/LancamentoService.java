package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoDetalhadoResponse;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;

import java.util.UUID;

public interface LancamentoService {
    LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest);
    LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento);
}
