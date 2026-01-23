package com.management.finito.meta.infra;

import com.management.finito.meta.domain.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetaJPARepository extends JpaRepository<Meta, UUID> {
}
