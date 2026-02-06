package com.management.finito.meta.application.repository;

import com.management.finito.meta.domain.Etapa;

import java.util.ArrayList;
import java.util.UUID;

public interface EtapaRepository {
    void salvaEtapas(ArrayList<Etapa> etapasParaSalvar);
    ArrayList<Etapa> buscaEtapas(UUID idMeta);
    void deletaEtapa(UUID idEtapa);
    Etapa buscaEtapa(UUID idEtapa);
    void salvaEtapa(Etapa etapaBuscada);
}
