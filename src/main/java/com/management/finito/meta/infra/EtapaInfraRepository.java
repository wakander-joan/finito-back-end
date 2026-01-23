package com.management.finito.meta.infra;

import com.management.finito.handler.APIException;
import com.management.finito.meta.application.repository.EtapaRepository;
import com.management.finito.meta.domain.Etapa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

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

    @Override
    public ArrayList<Etapa> buscaEtapas(UUID idMeta) {
        log.info("[start] EtapaInfraRepository - buscaEtapas");
        ArrayList<Etapa> etapas = etapaJPARepository.findByIdMeta(idMeta);
        log.info("[finish] EtapaInfraRepository - buscaEtapas");
        return etapas;
    }
}
