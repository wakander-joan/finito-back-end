package com.management.finito.feedback;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackRequest {
    private String tipo;
    private String nome;
    private String email;
    private String mensagem;
}
