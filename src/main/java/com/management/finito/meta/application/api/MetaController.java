package com.management.finito.meta.application.api;

import com.management.finito.meta.application.service.MetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MetaController implements MetaAPI {
    private final MetaService metaService;

    @Override
    public MetaResponse createMeta(MetaRequest metaRequest) {
        log.info("[start] MetaController - createPlanning");
        MetaResponse meta = metaService.createMeta(metaRequest);
        log.info("[finish] MetaController - createPlanning");
        return meta;
    }

    @Override
    public MetaDatailResponse getMetaId(int id) {
        log.info("[start] MetaController - getPlanningId");
        MetaDatailResponse meta = metaService.getMeta(id);
        log.info("[finish] MetaController - getPlanningId");
        return meta;
    }

    @Override
    public List<MetaListResponse> getAllMetaUser(UUID idUsuario) {
        log.info("[start] MetaController - getAllPlanning");
        List<MetaListResponse> metaListResponse = metaService.getAllMetaUser(idUsuario);
        log.info("[finish] MetaController - getAllPlanning");
        return metaListResponse;
    }


    @Override
    public void deleteMetaId(int id) {
        log.info("[start] MetaController - deletePlanningId");
        metaService.deleteMetaId(id);
        log.info("[finish] MetaController - deletePlanningId");
    }
}
