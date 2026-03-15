package com.management.finito.lancamento.application.api;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

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
    @NotNull
    private List<UUID> idsLancamentos;

}
