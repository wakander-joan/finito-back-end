package com.management.finito.lancamento.infra;

import com.management.finito.lancamento.domain.IdRecorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecorrenciaInfraJPARespository extends JpaRepository<IdRecorrencia, Integer> {
}
