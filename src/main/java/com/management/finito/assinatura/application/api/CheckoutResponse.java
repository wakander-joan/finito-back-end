package com.management.finito.assinatura.application.api;

import lombok.Getter;

/** URL da fatura hospedada do Asaas para onde o front redireciona o usuário. */
@Getter
public class CheckoutResponse {
    private final String url;

    public CheckoutResponse(String url) {
        this.url = url;
    }
}
