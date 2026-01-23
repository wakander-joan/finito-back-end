package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.Etapa;
import com.management.finito.meta.domain.Meta;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@ToString
public class MetaDetalhadaResponse {
    private UUID idMeta;
    private String descricao;
    private String anotacao;
    private int totalEtapas;
    private int totalEtapasConcluidas;
    private LocalDate dataInicial;
    private LocalDate dataAlvo;
    private ArrayList<Etapa> etapas;

    public MetaDetalhadaResponse(Meta metaBuscada, ArrayList<Etapa> etapasBuscadas) {
        this.idMeta = metaBuscada.getIdMeta();
        this.descricao = metaBuscada.getDescricao();
        this.anotacao = metaBuscada.getAnotacao();
        this.totalEtapas = metaBuscada.getTotalEtapas();
        this.totalEtapasConcluidas = metaBuscada.getTotalEtapasConcluidas();
        this.dataInicial = metaBuscada.getDataInicial();
        this.dataAlvo = metaBuscada.getDataAlvo();
        this.etapas = etapasBuscadas;
    }
}
