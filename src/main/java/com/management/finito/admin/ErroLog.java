package com.management.finito.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/** Registro de um erro da API (para o painel Ordersys Admin). */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ErroLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    private Instant quando;
    private String metodo;
    @Column(length = 600)
    private String path;
    private int status;
    @Column(columnDefinition = "text")
    private String mensagem;

    /** E-mail do usuário autenticado que fez a requisição (null se não autenticado). */
    private String usuarioEmail;

    /** Descrição amigável do que o usuário tentava fazer (ex.: "Criar lançamento"). */
    @Column(length = 300)
    private String acao;
}
