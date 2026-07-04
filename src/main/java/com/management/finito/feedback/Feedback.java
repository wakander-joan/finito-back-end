package com.management.finito.feedback;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/** Feedback enviado por um usuário (RECLAMACAO, SUGESTAO ou CONTATO). */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String tipo;   // RECLAMACAO | SUGESTAO | CONTATO
    private String nome;
    private String email;
    @Column(columnDefinition = "text")
    private String mensagem;
    private Instant quando;
    private boolean lido;
}
