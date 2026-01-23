package com.management.finito.meta.application.repository;

import com.management.finito.meta.domain.Meta;

import java.util.List;
import java.util.UUID;

public interface MetasRepository {
    Meta salvaMeta(Meta metaCriada);
    List<Meta> buscaMetas(UUID idUsuario);
}
