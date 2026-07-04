package com.management.finito.assinatura.application.api;

import com.management.finito.assinatura.domain.Assinatura;
import com.management.finito.assinatura.domain.StatusAssinatura;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Estado do premium do usuário, consumido pelo front (badge, tela Premium). */
@Getter
public class AssinaturaResponse {
    private final boolean premium;
    private final String status;
    private final LocalDate vigenteAte;
    private final String plano;
    private final BigDecimal valor;

    public AssinaturaResponse(boolean premium, Assinatura assinatura, BigDecimal valor) {
        this.premium = premium;
        this.status = assinatura != null ? assinatura.getStatus().name() : StatusAssinatura.SEM_ASSINATURA.name();
        this.vigenteAte = assinatura != null ? assinatura.getVigenteAte() : null;
        this.plano = assinatura != null ? assinatura.getPlano() : "PREMIUM_MENSAL";
        this.valor = valor;
    }
}
