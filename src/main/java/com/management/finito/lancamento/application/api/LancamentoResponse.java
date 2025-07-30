package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Getter
@ToString
public class LancamentoResponse {
    private UUID idLancamento;

    public LancamentoResponse(Lancamento lancamentoCriado) {
        this.idLancamento = lancamentoCriado.getIdLancamento();
    }
}
