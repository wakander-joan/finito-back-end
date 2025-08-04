package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoDetalhadoResponse;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;

import java.util.List;
import java.util.UUID;

public interface LancamentoService {
    LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest, MesDoLancamento mes, int ano);
    LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento);
    List<LancamentoDetalhadoResponse> buscaTodosLancamentoPorMes(MesDoLancamento mes);

    void deletaLancamento(UUID idLancamento);
}
