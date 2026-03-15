package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.Meta;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class MetaResponse {
    private UUID idMeta;

    public MetaResponse(Meta metaCriada) {
        this.idMeta = metaCriada.getIdMeta();
    }
}
