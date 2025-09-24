package com.management.finito.meta.domain;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.meta.application.api.MetaRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Log4j2
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDate dataInicial;
    private LocalDate dataAlvo;
    private Double valorInicial;
    private Double valorAlvo;
    private int parcelasPagas;
    private int parcelasTotais;
    private UUID idUsuario;

    public Meta(MetaRequest metaRequest) {
        log.info("[start] Meta - setaMeta");
        this.description = metaRequest.getDescription();
        this.dataInicial = metaRequest.getDataInicial();
        this.dataAlvo = metaRequest.getDataAlvo();
        this.valorInicial = metaRequest.getValorInicial();
        this.valorAlvo = metaRequest.getValorAlvo();
        this.idUsuario = metaRequest.getIdUsuario();
        log.info("[finish] Meta - setaMeta");
    }

    public void alteraParcelasTotais(List<Lancamento> lancamentos) {
        lancamentos.forEach(c -> parcelasTotais++);
    }

    public void addParcelaPaga() {
        this.parcelasPagas ++;
    }

    public void removeParcelaPaga() {
        if (this.parcelasPagas > 0) { // Impede que fique com valor negativo!
            this.parcelasPagas--;
        }
    }

    public void removeTotalDeParcelas(Double preco) {
        if (this.parcelasTotais > 0) { // Impede que fique com valor negativo!
            this.parcelasTotais--;
        }
        this.valorAlvo = valorAlvo - preco;
    }
}
