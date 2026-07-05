package com.management.finito.integracao;

import java.util.List;

/** Resumo financeiro anual (12 meses) de um usuário do Finito. */
public record ResumoAnualResponse(int ano, List<ResumoMesResponse> meses) {}
