package com.management.finito.lancamento.domain.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
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

    public static String fromId(int id) {
        return Arrays.stream(MesDoLancamento.values())
                .filter(m -> m.getId() == id)
                .map(MesDoLancamento::getMes)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mês inválido para id: " + id));
    }

    public static MesDoLancamento fromMes(int id) {
        return Arrays.stream(MesDoLancamento.values())
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mês inválido para id: " + id));
    }
}
