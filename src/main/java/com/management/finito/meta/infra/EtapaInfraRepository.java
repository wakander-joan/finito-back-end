package com.management.finito.meta.infra;

import com.management.finito.meta.application.repository.EtapaRepository;
import com.management.finito.meta.domain.Etapa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Log4j2
@RequiredArgsConstructor
public class EtapaInfraRepository implements EtapaRepository {
    private final EtapaJPARepository etapaJPARepository;

    @Override
    public void salvaEtapas(ArrayList<Etapa> etapasParaSalvar) {
        log.info("[start] EtapaInfraRepository - salvaEtapas");
        etapaJPARepository.saveAll(etapasParaSalvar);
        log.info("[finish] EtapaInfraRepository - salvaEtapas");
    }
}
