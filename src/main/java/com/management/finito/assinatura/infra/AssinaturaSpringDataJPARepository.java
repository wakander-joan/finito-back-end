package com.management.finito.assinatura.infra;

import com.management.finito.assinatura.domain.Assinatura;
import com.management.finito.assinatura.domain.StatusAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AssinaturaSpringDataJPARepository extends JpaRepository<Assinatura, UUID> {
    Optional<Assinatura> findByIdUsuario(UUID idUsuario);
    Optional<Assinatura> findByAsaasSubscriptionId(String asaasSubscriptionId);
    Optional<Assinatura> findByAsaasCustomerId(String asaasCustomerId);
    long countByStatusAndVigenteAteGreaterThanEqual(StatusAssinatura status, LocalDate data);
}
