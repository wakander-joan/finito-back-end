package com.management.finito.integracao;

/** Resumo financeiro de um mês (consumido pelo TaskFlow no Planejamento). */
public record ResumoMesResponse(
        int mes,
        double receitas,
        double despesas,
        double saldo,
        double parcelasMetas,
        double disponivel
) {}
