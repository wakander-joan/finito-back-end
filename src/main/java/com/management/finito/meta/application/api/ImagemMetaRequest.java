package com.management.finito.meta.application.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Corpo do PATCH que grava a imagem da meta (data URL comprimida ou URL http). */
@Getter
@Setter
@NoArgsConstructor
public class ImagemMetaRequest {
    private String imagem;
}
