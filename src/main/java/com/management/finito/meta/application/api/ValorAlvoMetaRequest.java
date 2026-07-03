package com.management.finito.meta.application.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Corpo do PATCH que grava o valor-alvo FIXO da meta (soma planejada das parcelas). */
@Getter
@Setter
@NoArgsConstructor
public class ValorAlvoMetaRequest {
    private Double valorAlvo;
}
