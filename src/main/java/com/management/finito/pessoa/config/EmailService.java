package com.management.finito.pessoa.config;

public interface EmailService {
    void enviarEmail(String para, String assunto, String mensagem);
}