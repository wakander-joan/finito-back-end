package com.management.finito.assinatura.application.repository;

import com.management.finito.assinatura.domain.Assinatura;

import java.util.Optional;
import java.util.UUID;

public interface AssinaturaRepository {
    Assinatura salva(Assinatura assinatura);
    Optional<Assinatura> buscaPorUsuario(UUID idUsuario);
    Optional<Assinatura> buscaPorAssinaturaAsaas(String asaasSubscriptionId);
    Optional<Assinatura> buscaPorCustomerAsaas(String asaasCustomerId);
}
