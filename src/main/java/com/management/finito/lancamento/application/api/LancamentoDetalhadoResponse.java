package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@ToString
@Getter
public class LancamentoDetalhadoResponse {
    private UUID idLancamento;
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private SatatusLancamento status;
    private TipoLancamento tipo;
    private MesDoLancamento mesDoLancamento;

    public LancamentoDetalhadoResponse(Lancamento lancamento) {
        this.idLancamento = lancamento.getIdLancamento();
        this.descricao = lancamento.getDescricao();
        this.preco = lancamento.getPreco();
        this.dataVencimento = lancamento.getDataVencimento();
        this.status = lancamento.getStatus();
        this.tipo = lancamento.getTipo();
        this.mesDoLancamento = lancamento.getMesDoLancamento();
    }
}
