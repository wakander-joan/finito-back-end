package com.management.finito.lancamento.domain.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
public enum CategoriaLancamento {

    // Despesas
    MORADIA             (1, "Moradia"),
    TRANSPORTE          (2, "Transporte"),
    ALIMENTACAO         (3, "Alimentação"),
    SAUDE               (4, "Saúde"),
    EDUCACAO            (5, "Educação"),
    LAZER               (6, "Lazer"),
    VESTUARIO           (7, "Vestuário"),
    SERVICOS            (8, "Serviços"),
    PETS                (9, "Animais de Estimação"),
    IMPOSTOS            (10, "Impostos e Taxas"),
    OUTRAS_DESPESAS     (11, "Outras Despesas"),

    // Receitas
    SALARIO             (12, "Salário"),
    FREELANCE           (13, "Freelance"),
    ALUGUEL_RECEBIDO    (14, "Aluguel Recebido"),
    INVESTIMENTOS       (15, "Investimentos"),
    REEMBOLSOS          (16, "Reembolsos"),
    PREMIOS             (17, "Prêmios"),
    VENDAS              (18, "Vendas"),
    AJUDAS              (19, "Ajudas"),
    OUTRAS_RECEITAS     (20, "Outras Receitas");

    private final int id;
    private final String descricao;

    CategoriaLancamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static String fromId(int id) {
        return Arrays.stream(CategoriaLancamento.values())
                .filter(c -> c.getId() == id)
                .map(CategoriaLancamento::getDescricao)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoria inválida para id: " + id));
    }
}
