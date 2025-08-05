package com.management.finito.lancamento.domain;

import com.management.finito.lancamento.application.api.LancamentoAlteracaoRequest;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Log4j2
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
    private int ano;
    private int categoriaLancamento;

    public Lancamento(LancamentoRequest lancamentoRequest, UUID idPessoa, MesDoLancamento mes, int ano) {
        this.idPessoa = idPessoa;
        this.descricao = lancamentoRequest.getDescricao();
        this.preco = lancamentoRequest.getPreco();
        this.dataVencimento = lancamentoRequest.getDataVencimento();
        this.status = lancamentoRequest.getStatus().getId();
        this.tipo = lancamentoRequest.getTipo().getId();
        this.mesDoLancamento = mes.getId();
        this.ano = ano;
        this.categoriaLancamento = lancamentoRequest.getCategoriaLancamento().getId();
    }

    public void atualiza(LancamentoAlteracaoRequest lancamentoAlteracaoRequest) {
        log.info("[start] Lancamento - atualiza");
        this.descricao = lancamentoAlteracaoRequest.getDescricao();
        this.preco = lancamentoAlteracaoRequest.getPreco();
        this.dataVencimento = lancamentoAlteracaoRequest.getDataVencimento();
        this.tipo = lancamentoAlteracaoRequest.getTipo().getId();
        this.categoriaLancamento = lancamentoAlteracaoRequest.getCategoriaLancamento().getId();
        log.info("[finish] Lancamento - atualiza");
    }

    public void mudaStatusParaPendente() {
        log.info("[start] Lancamento - mudaStatusParaPendente");
        this.status = 2;
        log.info("[finish] Lancamento - mudaStatusParaPendente");
    }

    public void mudaStatusParaPago() {
        log.info("[start] Lancamento - mudaStatusParaPago");
        this.status = 1;
        log.info("[finish] Lancamento - mudaStatusParaPago");
    }
}
