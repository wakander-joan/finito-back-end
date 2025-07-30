package com.management.finito.lancamento.domain.enums;

public enum MesDoLancamento {
    JANEIRO     (1, "JANEIRO"),
    FEVEREIRO   (2, "FEVEREIRO"),
    MARCO       (3, "MARÇO"),
    ABRIL       (4, "ABRIL"),
    MAIO        (5, "MAIO"),
    JUNHO       (6, "JUNHO"),
    JULHO       (7, "JULHO"),
    AGOSTO      (8, "AGOSTO"),
    SETEMBRO    (9, "SETEMBRO"),
    OUTUBRO     (10, "OUTUBRO"),
    NOVEMBRO    (11, "NOVEMBRO"),
    DEZEMBRO    (12, "DEZEMBRO");

    private final int id;
    private final String mes;

    MesDoLancamento(int id, String mes) {
        this.id = id;
        this.mes = mes;
    }
}
