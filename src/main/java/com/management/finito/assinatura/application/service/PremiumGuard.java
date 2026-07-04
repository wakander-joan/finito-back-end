package com.management.finito.assinatura.application.service;

import com.management.finito.assinatura.application.repository.AssinaturaRepository;
import com.management.finito.assinatura.domain.Assinatura;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Ponto único para checar Premium e os limites do plano grátis.
 * Usado pelos serviços de Meta e Lançamento para bloquear a criação além do limite.
 */
@Component
@RequiredArgsConstructor
public class PremiumGuard {
    private final AssinaturaRepository assinaturaRepository;

    @Value("${premium.limite-metas:2}")
    private int limiteMetas;

    @Value("${premium.limite-lancamentos-mes:40}")
    private int limiteLancamentosMes;

    public boolean isPremium(UUID idUsuario) {
        return assinaturaRepository.buscaPorUsuario(idUsuario).map(Assinatura::isPremium).orElse(false);
    }

    public int getLimiteMetas() {
        return limiteMetas;
    }

    public int getLimiteLancamentosMes() {
        return limiteLancamentosMes;
    }
}
