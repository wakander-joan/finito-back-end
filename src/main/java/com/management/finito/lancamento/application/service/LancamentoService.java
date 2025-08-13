package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.*;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;

import java.util.List;
import java.util.UUID;

public interface LancamentoService {
    LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest, MesDoLancamento mes, int ano);
    LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento);
    List<LancamentoDetalhadoResponse> buscaLancamentosPorMesEAno(MesDoLancamento mes, int ano);
    void deletaLancamento(UUID idLancamento);
    void editaLancamento(UUID idLancamento, LancamentoAlteracaoRequest lancamentoAlteracaoRequest);
    void mudaStatusParaPendente(UUID idLancamento);
    void mudaStatusParaPago(UUID idLancamento);
    void replicaLancamentos(ReplicaLancamentosRequest replicaLancamentosRequest);
}
