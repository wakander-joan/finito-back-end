package com.management.finito.pessoa.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public interface EmailService {
    void enviarEmail(String para, String assunto, String mensagem);
    void enviarEmailHtml(@Email @NotNull String email, String assunto, String mensagem);
}