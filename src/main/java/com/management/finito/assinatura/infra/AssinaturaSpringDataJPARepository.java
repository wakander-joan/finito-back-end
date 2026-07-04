package com.management.finito.assinatura.infra;

import com.management.finito.assinatura.domain.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssinaturaSpringDataJPARepository extends JpaRepository<Assinatura, UUID> {
    Optional<Assinatura> findByIdUsuario(UUID idUsuario);
    Optional<Assinatura> findByAsaasSubscriptionId(String asaasSubscriptionId);
    Optional<Assinatura> findByAsaasCustomerId(String asaasCustomerId);
}
