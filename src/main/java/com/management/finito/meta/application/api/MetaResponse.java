package com.management.finito.meta.application.api;

import com.management.finito.meta.domain.Meta;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
public class MetaResponse {
    private int id;

    public MetaResponse(Meta meta) {
        this.id = meta.getId();
    }
}
