package com.management.finito.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ErroLogJPARepository extends JpaRepository<ErroLog, UUID> {
    List<ErroLog> findTop50ByOrderByQuandoDesc();
}
