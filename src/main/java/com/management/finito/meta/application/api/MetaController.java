package com.management.finito.meta.application.api;

import com.management.finito.meta.application.service.MetaService;
import com.management.finito.meta.domain.StatusEtapa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Log4j2
public class MetaController implements MetaAPI {
    private final MetaService metaService;


    @Override
    public MetaResponse cadastraMeta(MetaRequest metatoRequest) {
        log.info("[start] MetaController - cadastraMeta");
        MetaResponse metaCriada = metaService.cadastraMeta(metatoRequest);
        log.info("[finish] MetaController - cadastraMeta");
        return metaCriada;
    }

    @Override
    public List<MetaDetalhadaResponse> buscaMetas() {
        log.info("[start] MetaController - buscaMeta");
        List<MetaDetalhadaResponse> metaDetasdetalhada = metaService.buscaMetas();
        log.info("[finish] MetaController - buscaMeta");
        return metaDetasdetalhada;
    }

    @Override
    public void deletaMeta(UUID idMeta) {
        log.info("[start] MetaController - deletaMeta");
        metaService.deletaMeta(idMeta);
        log.info("[finish] MetaController - deletaMeta");
    }

    @Override
    public void alteraStatusEtapa(UUID idEtapa, String status) {
        log.info("[start] MetaController - alteraStatusEtapa");
        log.info("status={}", status);
        metaService.alteraStatusEtapa(idEtapa, status);
        log.info("[finish] MetaController - alteraStatusEtapa");
    }

}
