package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.Meta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MetaListResponse {
    private int id;
    private String description;
    private LocalDate dataInicial;
    private LocalDate dataAlvo;
    private Double valorInicial;
    private Double valorAlvo;
    private int parcelasPagas;
    private int parcelasTotais;


    public MetaListResponse(Meta meta) {
        this.id = meta.getId();
        this.description = meta.getDescription();
        this.dataInicial = meta.getDataInicial();
        this.dataAlvo = meta.getDataAlvo();
        this.valorInicial = meta.getValorInicial();
        this.valorAlvo = meta.getValorAlvo();
        this.parcelasPagas = meta.getParcelasPagas();
        this.parcelasTotais = meta.getParcelasTotais();
    }

    public static List<MetaListResponse> convert(List<Meta> metas) {
        return metas.stream().map(MetaListResponse::new).collect(Collectors.toList());
    }
}
