package com.management.finito.lancamento.application.api;

import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Setter
public class LancamentoAlteracaoRequest {
    private String descricao;
    private Double preco;
    private LocalDate dataVencimento;
    private TipoLancamento tipo;
    private CategoriaLancamento categoriaLancamento;
    private String anotacao;
}
