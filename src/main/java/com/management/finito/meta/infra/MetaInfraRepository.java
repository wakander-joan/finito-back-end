package com.management.finito.meta.infra;

import com.management.finito.handler.APIException;
import com.management.finito.meta.application.repository.MetaRepository;
import com.management.finito.meta.domain.Meta;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class MetaInfraRepository implements MetaRepository {
    private final MetaJPARepository metaJPARepository;

    @Override
    public Meta saveMeta(Meta meta) {
        log.info("[start] MetaInfraRepository - createPlanning");
        Meta metabuscada = metaJPARepository.save(meta);
        log.info("[finish] MetaInfraRepository - createPlanning");
        return metabuscada;
    }

    @Override
    public Meta getMetaId(int id) {
        log.info("[start] MetaInfraRepository - getPlanningId");
        Meta meta = metaJPARepository.findById(id)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Peça não encontrada"));
        log.info("[finish] MetaInfraRepository - getPlanningId");
        return meta;
    }

    @Override
    public List<Meta> getAllMetas(UUID idUsuario) {
        log.info("[start] MetaInfraRepository - getAllPlanning");
        List<Meta> metas = metaJPARepository.findAllByIdUsuario(idUsuario);
        log.info("[finish] MetaInfraRepository - getAllPlanning");
        return metas;
    }

    @Override
    public void deleteMetaId(int id) {
        log.info("[start] MetaInfraRepository - deletePlanningId");
        metaJPARepository.deleteById(id);
        log.info("[finish] MetaInfraRepository - deletePlanningId");
    }

}
