package com.management.finito.assinatura.application.service;

import com.management.finito.assinatura.application.api.AssinaturaResponse;
import com.management.finito.assinatura.application.api.CheckoutRequest;
import com.management.finito.assinatura.application.api.CheckoutResponse;

import java.util.Map;

public interface AssinaturaService {
    CheckoutResponse checkout(CheckoutRequest request);
    AssinaturaResponse statusDoUsuarioLogado();
    void processarWebhook(String event, Map<String, Object> payment);
}
