package com.management.finito.meta.application.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Getter
@ToString
public class MetaRequest {
    @NotBlank
    private String descricao;
    @NotBlank
    private String anotacao;
    @NotNull
    private LocalDate dataInicial;
    @NotNull
    private LocalDate dataAlvo;
    @NotNull
    private ArrayList<EtapaRequest> etapas;
}
