package com.management.finito.assinatura.infra;

import com.management.finito.assinatura.application.repository.AssinaturaRepository;
import com.management.finito.assinatura.domain.Assinatura;
import com.management.finito.assinatura.domain.StatusAssinatura;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@Log4j2
@RequiredArgsConstructor
public class AssinaturaInfraRepository implements AssinaturaRepository {
    private final AssinaturaSpringDataJPARepository jpa;

    @Override
    public Assinatura salva(Assinatura assinatura) {
        log.info("[start] AssinaturaInfraRepository - salva");
        Assinatura salva = jpa.save(assinatura);
        log.info("[finish] AssinaturaInfraRepository - salva");
        return salva;
    }

    @Override
    public Optional<Assinatura> buscaPorUsuario(UUID idUsuario) {
        return jpa.findByIdUsuario(idUsuario);
    }

    @Override
    public Optional<Assinatura> buscaPorAssinaturaAsaas(String asaasSubscriptionId) {
        return jpa.findByAsaasSubscriptionId(asaasSubscriptionId);
    }

    @Override
    public Optional<Assinatura> buscaPorCustomerAsaas(String asaasCustomerId) {
        return jpa.findByAsaasCustomerId(asaasCustomerId);
    }

    @Override
    public long contaPremiumAtivas() {
        return jpa.countByStatusAndVigenteAteGreaterThanEqual(StatusAssinatura.ATIVA, LocalDate.now());
    }
}
