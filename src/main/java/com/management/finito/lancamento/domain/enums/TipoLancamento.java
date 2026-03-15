package com.management.finito.lancamento.domain.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
public enum TipoLancamento {
    RECEITA (1, "RECEITA"),
    DESPESA (2, "DESPESA");

    private Integer id;
    private String tipo;

    TipoLancamento(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public static String fromId(Integer id) {
        return Arrays.stream(TipoLancamento.values())
                .filter(t -> t.getId().equals(id))
                .map(TipoLancamento::getTipo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo inválido para id: " + id));
    }
}
