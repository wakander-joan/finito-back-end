package com.management.finito.meta.infra;

import com.management.finito.handler.APIException;
import com.management.finito.meta.application.repository.MetasRepository;
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
public class MetasInfraRepository implements MetasRepository {
    private final MetaJPARepository metaJPARepository;

    @Override
    public Meta salvaMeta(Meta metaCriada) {
        log.info("[start] MetasInfraRepository - salvaMeta");
        Meta metaSalva = metaJPARepository.save(metaCriada);
        log.info("[finish] MetasInfraRepository - salvaMeta");
        return metaSalva;
    }

    @Override
    public List<Meta> buscaMetas(UUID idUsuario) {
        log.info("[start] MetasInfraRepository - buscaMeta");
        List<Meta> metas = metaJPARepository.findAllByIdUsuario(idUsuario);
        log.info("[finish] MetasInfraRepository - buscaMeta");
        return metas;
    }
}
