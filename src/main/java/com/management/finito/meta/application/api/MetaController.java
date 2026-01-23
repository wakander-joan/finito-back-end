package com.management.finito.meta.application.api;

import com.management.finito.meta.application.service.MetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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

}
