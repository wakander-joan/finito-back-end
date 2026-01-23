package com.management.finito.meta.application.service;

import com.management.finito.meta.application.api.MetaRequest;
import com.management.finito.meta.application.api.MetaResponse;

public interface MetaService {
    MetaResponse cadastraMeta(MetaRequest metatoRequest);
}
