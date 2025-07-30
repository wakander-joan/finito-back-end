package com.management.finito.lancamento.domain.enums;

import lombok.Getter;

@Getter
public enum TipoLancamento {
    RECEITA (1, "RECEITA"),
    DESPESA (1, "DESPESA");

    private Integer id;
    private String tipo;

    TipoLancamento(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }
}
