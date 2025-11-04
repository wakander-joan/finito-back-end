package com.management.finito.lancamento.domain;

import com.management.finito.lancamento.application.api.LancamentoAlteracaoRequest;
import com.management.finito.lancamento.application.api.LancamentoDetalhadoResponse;
import com.management.finito.lancamento.application.api.LancamentoEmLoteRequest;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
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
    private int idMeta = 0;
    private int idRecorrencia = 0;
    private int idParcela = 0;

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


    //Construtor de Recorrencia
    public Lancamento(Lancamento lancamento, int mes, Integer idRecorrencia) {
        this.idPessoa = lancamento.getIdPessoa();
        this.descricao = lancamento.getDescricao();
        this.preco = lancamento.getPreco();
        this.status = lancamento.getStatus();
        this.tipo = lancamento.getTipo();
        this.ano = lancamento.getAno();
        this.categoriaLancamento = lancamento.categoriaLancamento;
        this.idMeta = lancamento.getIdMeta();
        this.idRecorrencia = idRecorrencia;

        this.mesDoLancamento = mes;

        int ano = lancamento.getDataVencimento().getYear();
        int dia = lancamento.getDataVencimento().getDayOfMonth();

        YearMonth ym = YearMonth.of(ano, mes);
        int diasNoMes = ym.lengthOfMonth();		// 30

        if (dia > diasNoMes) {
            this.dataVencimento = LocalDate.of(ano, mes, diasNoMes);
        } else {
            this.dataVencimento = LocalDate.of(ano, mes, dia);
        }
    }

    //Construtor de Parcelado
    public Lancamento(Lancamento lancamento, int mes, int anoAdd, String descricao) {
        this.idPessoa = lancamento.getIdPessoa();
        this.descricao = descricao;
        this.preco = lancamento.getPreco();
        this.status = lancamento.getStatus();
        this.tipo = lancamento.getTipo();
        this.ano = anoAdd;
        this.categoriaLancamento = lancamento.categoriaLancamento;
        this.idMeta = lancamento.getIdMeta();
        this.idParcela = lancamento.getIdParcela();

        this.mesDoLancamento = mes;

        int anoCalcula = lancamento.getDataVencimento().getYear();
        int dia = lancamento.getDataVencimento().getDayOfMonth();

        YearMonth ym = YearMonth.of(anoCalcula, mes);
        int diasNoMes = ym.lengthOfMonth();		// 30

        if (dia > diasNoMes) {
            this.dataVencimento = LocalDate.of(ano, mes, diasNoMes);
        } else {
            this.dataVencimento = LocalDate.of(ano, mes, dia);
        }
    }

    public Lancamento(Lancamento lancamento) {
        this.idPessoa = lancamento.getIdPessoa();
        this.descricao = lancamento.getDescricao();
        this.preco = lancamento.getPreco();
        this.dataVencimento = lancamento.getDataVencimento();
        this.status = lancamento.getStatus();
        this.tipo = lancamento.getTipo();
        this.mesDoLancamento = lancamento.getMesDoLancamento();
        this.ano = lancamento.getAno();
        this.categoriaLancamento = lancamento.categoriaLancamento;
        this.idMeta = lancamento.getIdMeta();
    }

    public Lancamento(Lancamento lancamento, String descricao1) {
        this.idPessoa = lancamento.getIdPessoa();
        this.descricao = descricao1;
        this.preco = lancamento.getPreco();
        this.dataVencimento = lancamento.getDataVencimento();
        this.status = lancamento.getStatus();
        this.tipo = lancamento.getTipo();
        this.mesDoLancamento = lancamento.getMesDoLancamento();
        this.ano = lancamento.getAno();
        this.categoriaLancamento = lancamento.categoriaLancamento;
        this.idMeta = lancamento.getIdMeta();
    }

    public Lancamento(@Valid LancamentoEmLoteRequest c, int idMeta, UUID idPessoa) {
        this.idPessoa = idPessoa;
        this.descricao = c.getDescricao();
        this.preco = c.getPreco();
        this.dataVencimento = c.getDataVencimento();
        this.status = c.getStatus().getId();
        this.tipo = c.getTipo().getId();
        this.mesDoLancamento = c.getMesDoLancamento();
        this.ano = c.getAno();
        this.categoriaLancamento = c.getCategoriaLancamento().getId();
        this.idMeta = idMeta;
    }

    public static List<Lancamento> criaLancamentosEmLote(List<@Valid LancamentoEmLoteRequest> lancamentosEmLoteRequest, int idMeta, UUID idPessoa) {
        return lancamentosEmLoteRequest.stream().map(c -> new Lancamento(c, idMeta, idPessoa)).collect(Collectors.toList());
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
