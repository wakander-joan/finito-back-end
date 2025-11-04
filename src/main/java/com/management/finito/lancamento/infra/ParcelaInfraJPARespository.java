package com.management.finito.lancamento.infra;

import com.management.finito.lancamento.domain.IdParcela;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaInfraJPARespository extends JpaRepository<IdParcela, Integer>{
}