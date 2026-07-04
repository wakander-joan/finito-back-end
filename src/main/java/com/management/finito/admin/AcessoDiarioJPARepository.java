package com.management.finito.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AcessoDiarioJPARepository extends JpaRepository<AcessoDiario, LocalDate> {
    List<AcessoDiario> findByDiaGreaterThanEqualOrderByDiaAsc(LocalDate desde);
}
