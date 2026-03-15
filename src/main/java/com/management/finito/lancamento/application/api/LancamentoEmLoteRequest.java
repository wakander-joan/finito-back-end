package com.management.finito.lancamento.application.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.SatatusLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
public class LancamentoEmLoteRequest {
    @NotBlank
    private String descricao;
    @NotNull
    private Double preco;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;
    @NotNull
    private SatatusLancamento status;
    @NotNull
    private TipoLancamento tipo;
    @NotNull
    private CategoriaLancamento categoriaLancamento;
    @NotNull
    private int mesDoLancamento;
    @NotNull
    private int ano;
}
