package com.management.finito.meta.application.api;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EtapaRequest {
    private int numero;
    private String descricao;
    private String anotacao;
}
