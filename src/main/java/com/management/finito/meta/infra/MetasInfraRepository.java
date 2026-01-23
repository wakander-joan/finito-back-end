package com.management.finito.meta.infra;

import com.management.finito.meta.application.repository.MetasRepository;
import com.management.finito.meta.domain.Meta;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

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
}
