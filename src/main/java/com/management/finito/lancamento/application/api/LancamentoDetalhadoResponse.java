package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ToString
@Getter
public class LancamentoDetalhadoResponse {
    private UUID idLancamento;
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private String status;
    private String tipo;
    private String mesDoLancamento;
    private String categoriaLancamento;
    private int idRecorrencia;
    private int idMeta;
    private int idParcela;
    private String anotacao;

    public LancamentoDetalhadoResponse(Lancamento lancamento) {
        this.idLancamento = lancamento.getIdLancamento();
        this.descricao = lancamento.getDescricao();
        this.preco = lancamento.getPreco();
        this.dataVencimento = lancamento.getDataVencimento();
        this.status = SatatusLancamento.fromId(lancamento.getStatus());
        this.tipo = TipoLancamento.fromId(lancamento.getTipo());
        this.mesDoLancamento = MesDoLancamento.fromId(lancamento.getMesDoLancamento());
        this.categoriaLancamento = CategoriaLancamento.fromId(lancamento.getCategoriaLancamento());
        this.idRecorrencia = lancamento.getIdRecorrencia();
        this.idMeta = lancamento.getIdMeta();
        this.idParcela = lancamento.getIdParcela();
        this.anotacao = lancamento.getAnotacao();
    }

    public static List<LancamentoDetalhadoResponse> converte(List<Lancamento> lancamentos) {
        return lancamentos.stream().map(c -> new LancamentoDetalhadoResponse(c)).collect(Collectors.toList());
    }

}
