package com.management.finito.lancamento.application.api;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReplicaLancamentosRequest {
    @NotNull
    private int mesBase;
    @NotNull
    private int anoBase;
    @NotNull
    private int mesDestino;
    @NotNull
    private int anoDestino;
}
