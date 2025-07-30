package com.management.finito.lancamento.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID idLancamento;
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private SatatusLancamento status;
    private TipoLancamento tipo;
    private MesDoLancamento mesDoLancamento;

    public Lancamento(LancamentoRequest lancamentoRequest) {
        this.descricao = lancamentoRequest.getDescricao();
        this.preco = lancamentoRequest.getPreco();
        this.dataVencimento = lancamentoRequest.getDataVencimento();
        this.status = lancamentoRequest.getStatus();
        this.tipo = lancamentoRequest.getTipo();
        this.mesDoLancamento = lancamentoRequest.getMesDoLancamento();
    }
}
