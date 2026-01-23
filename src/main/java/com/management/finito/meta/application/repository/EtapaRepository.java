package com.management.finito.meta.application.repository;

import com.management.finito.meta.domain.Etapa;

import java.util.ArrayList;

public interface EtapaRepository {
    void salvaEtapas(ArrayList<Etapa> etapasParaSalvar);
}
