package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class LancamentoDetalhadoResponse {
    private UUID idLancamento;
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private SatatusLancamento status;
    private TipoLancamento tipo;
    private MesDoLancamento mesDoLancamento;
}
