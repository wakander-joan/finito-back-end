package com.management.finito.lancamento.domain.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
public enum SatatusLancamento {
    PAGO (1, "PAGO"),
    PENDENTE (2, "PENDENTE");

    private Integer id;
    private String status;

    SatatusLancamento(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public static SatatusLancamento fromId(Integer id) {
        return Arrays.stream(SatatusLancamento.values())
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status inválido para id: " + id));
    }
}
