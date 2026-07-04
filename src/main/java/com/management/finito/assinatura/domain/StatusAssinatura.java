package com.management.finito.assinatura.domain;

public enum StatusAssinatura {
    SEM_ASSINATURA,  // nunca assinou
    PENDENTE,        // checkout criado, aguardando pagamento
    ATIVA,           // pagamento confirmado, premium liberado
    ATRASADA,        // pagamento venceu (período de tolerância)
    CANCELADA        // cancelada / estornada
}
