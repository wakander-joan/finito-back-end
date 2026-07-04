package com.management.finito.assinatura.application.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Dados enviados pelo front para iniciar o checkout do Premium. */
@Getter
@Setter
@NoArgsConstructor
public class CheckoutRequest {
    private String cpfCnpj;    // exigido pelo Asaas para gerar Pix/boleto
    private String retornoUrl; // para onde o Asaas redireciona após o pagamento (ex.: <origin>/premium?pago=1)
}
