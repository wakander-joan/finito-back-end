package com.management.finito.lancamento.domain.enums;

import lombok.Getter;

@Getter
public enum SatatusLancamento {
    PAGO (1, "PAGO"),
    PENDENTE (2, "PENDENTE");

    private Integer id;
    private String status;

    SatatusLancamento(Integer id, String status) {
        this.id = id;
        this.status = status;
    }
}
