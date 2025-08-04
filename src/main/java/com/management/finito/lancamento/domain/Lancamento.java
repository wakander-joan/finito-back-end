package com.management.finito.lancamento.domain;

import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID idPessoa;
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private int status;
    private int tipo;
    private int mesDoLancamento;

    public Lancamento(LancamentoRequest lancamentoRequest, UUID idPessoa) {
        this.idPessoa = idPessoa;
        this.descricao = lancamentoRequest.getDescricao();
        this.preco = lancamentoRequest.getPreco();
        this.dataVencimento = lancamentoRequest.getDataVencimento();
        this.status = lancamentoRequest.getStatus().getId();
        this.tipo = lancamentoRequest.getTipo().getId();
        this.mesDoLancamento = lancamentoRequest.getMesDoLancamento().getId();
    }
}
