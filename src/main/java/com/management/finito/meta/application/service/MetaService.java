package com.management.finito.meta.application.service;



import com.management.finito.meta.application.api.*;

import java.util.List;
import java.util.UUID;

public interface MetaService {
    MetaResponse createMeta(MetaRequest metaRequest);
    MetaDatailResponse getMeta(int id);
    List<MetaListResponse> getAllMetaUser(UUID idUsuario);
    void deleteMetaId(int id);
}
