package com.management.finito.meta.application.repository;

import com.management.finito.meta.domain.Meta;

import java.util.List;
import java.util.UUID;

public interface MetaRepository {
    Meta saveMeta(Meta meta);
    Meta getMetaId(int id);
    List<Meta> getAllMetas(UUID idUsuario);
    void deleteMetaId(int id);
}
