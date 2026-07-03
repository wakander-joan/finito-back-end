package com.management.finito.meta.application.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Atualização do valor de uma parcela (etapa). O valor mora no campo `anotacao`
 * como string BRL (mesmo formato usado no cadastro), lido no front via parseBRL.
 */
@Getter
@Setter
@NoArgsConstructor
public class EtapaValorRequest {
    private UUID idEtapa;
    private String anotacao;
}
