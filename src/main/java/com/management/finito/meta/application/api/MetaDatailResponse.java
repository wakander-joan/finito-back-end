package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.Meta;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Getter
@ToString
@Log4j2
public class MetaDatailResponse {
    private int id;
    private String description;
    private LocalDate dataInicial;
    private LocalDate dataAlvo;
    private Double valorInicial;
    private Double valorAlvo;
    private int parcelasPagas;
    private int parcelasTotais;


    public MetaDatailResponse(Meta meta) {
        this.id = meta.getId();
        this.description = meta.getDescription();
        this.dataInicial = meta.getDataInicial();
        this.dataAlvo = meta.getDataAlvo();
        this.valorInicial = meta.getValorInicial();
        this.valorAlvo = meta.getValorAlvo();
        this.parcelasPagas = meta.getParcelasPagas();
        this.parcelasTotais = meta.getParcelasTotais();
    }
}
