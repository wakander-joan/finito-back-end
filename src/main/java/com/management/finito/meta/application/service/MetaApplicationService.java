package com.management.finito.meta.application.service;

import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.meta.application.api.MetaDatailResponse;
import com.management.finito.meta.application.api.MetaListResponse;
import com.management.finito.meta.application.api.MetaRequest;
import com.management.finito.meta.application.api.MetaResponse;
import com.management.finito.meta.application.repository.MetaRepository;
import com.management.finito.meta.domain.Meta;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Service
public class MetaApplicationService implements MetaService {
    private final MetaRepository metaRepository;
    private final LancamentoRepository lancamentoRepository;

    @Override
    public MetaResponse createMeta(MetaRequest metaRequest) {
        log.info("[start] MetaApplicationService - createPlanning");
        Meta meta = metaRepository.saveMeta(new Meta(metaRequest));
        log.info("[finish] MetaApplicationService - createPlanning");
        return new MetaResponse(meta);
    }

    @Override
    public MetaDatailResponse getMeta(int id) {
        log.info("[start] MetaApplicationService - getPlanningId");
        Meta meta = metaRepository.getMetaId(id);
        log.info("[finish] MetaApplicationService - getPlanningId");
        return new MetaDatailResponse(meta);
    }

    @Override
    public List<MetaListResponse> getAllMetaUser(UUID idUsuario) {
        log.info("[start] MetaApplicationService - getAllPlanning");
        List<Meta> metas = metaRepository.getAllMetas(idUsuario);
        log.info("[finish] MetaApplicationService - getAllPlanning");
        return MetaListResponse.convert(metas);
    }

    @Override
    public void deleteMetaId(int id) {
        log.info("[start] MetaApplicationService - deletePlanningId");
        metaRepository.getMetaId(id);
        metaRepository.deleteMetaId(id);
        lancamentoRepository.deleteAllLancamentosMeta(id);
        log.info("[finish] MetaApplicationService - deletePlanningId");
    }
}
