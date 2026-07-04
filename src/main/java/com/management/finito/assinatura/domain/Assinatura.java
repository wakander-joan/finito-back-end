package com.management.finito.assinatura.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Assinatura Premium de um usuário (integração com o Asaas).
 * O valor-alvo do plano fica na config (premium.valor); aqui guardamos o vínculo
 * com o Asaas (customer/subscription), o status e até quando o premium é válido.
 */
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Assinatura {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID idAssinatura;

    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID idUsuario;

    private String asaasCustomerId;      // cus_xxx
    private String asaasSubscriptionId;  // sub_xxx
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    private StatusAssinatura status = StatusAssinatura.SEM_ASSINATURA;

    private String plano = "PREMIUM_MENSAL";
    private LocalDate vigenteAte;        // premium válido até esta data (renovado a cada pagamento)
    private Instant criadoEm;
    private Instant atualizadoEm;

    public Assinatura(UUID idUsuario) {
        this.idUsuario = idUsuario;
        this.status = StatusAssinatura.SEM_ASSINATURA;
        this.plano = "PREMIUM_MENSAL";
        this.criadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }

    /** true se o usuário tem premium ativo agora (status ATIVA e ainda dentro da validade). */
    public boolean isPremium() {
        return status == StatusAssinatura.ATIVA
                && vigenteAte != null
                && !vigenteAte.isBefore(LocalDate.now());
    }

    /** Registra o customer/subscription do Asaas e marca como aguardando pagamento. */
    public void iniciarCheckout(String asaasCustomerId, String asaasSubscriptionId, String cpfCnpj) {
        this.asaasCustomerId = asaasCustomerId;
        this.asaasSubscriptionId = asaasSubscriptionId;
        this.cpfCnpj = cpfCnpj;
        if (this.status != StatusAssinatura.ATIVA) this.status = StatusAssinatura.PENDENTE;
        this.atualizadoEm = Instant.now();
    }

    public void ativar(LocalDate vigenteAte) {
        this.status = StatusAssinatura.ATIVA;
        this.vigenteAte = vigenteAte;
        this.atualizadoEm = Instant.now();
    }

    public void marcarAtrasada() {
        this.status = StatusAssinatura.ATRASADA;
        this.atualizadoEm = Instant.now();
    }

    public void cancelar() {
        this.status = StatusAssinatura.CANCELADA;
        this.atualizadoEm = Instant.now();
    }
}
