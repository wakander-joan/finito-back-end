package com.management.finito.meta.infra;

import com.management.finito.meta.domain.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.UUID;

public interface EtapaJPARepository extends JpaRepository<Etapa, UUID> {
    ArrayList<Etapa> findByIdMeta(UUID idMeta);
}
