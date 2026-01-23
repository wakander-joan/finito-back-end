package com.management.finito.meta.application.service;

import com.management.finito.meta.application.api.MetaDetalhadaResponse;
import com.management.finito.meta.application.api.MetaRequest;
import com.management.finito.meta.application.api.MetaResponse;

import java.util.List;
import java.util.UUID;

public interface MetaService {
    MetaResponse cadastraMeta(MetaRequest metatoRequest);
    List<MetaDetalhadaResponse> buscaMeta();
}
