package com.management.finito.pessoa.config;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    private static final String HTML_TEMPLATE = """
        <html>
          <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 30px;">
            <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);">
              <h2 style="color: #2e7d32; text-align: center;">Bem-vindo(a) ao Finito!</h2>
              <p style="font-size: 16px; color: #333;">
                Olá, tudo bem? <br/><br/>
                Seu cadastro foi <strong>realizado com sucesso</strong> e agora você faz parte da nossa comunidade!
              </p>
              <p style="font-size: 16px; color: #333;">
                Estamos muito felizes em ter você conosco. Acompanhe seus lançamentos, organize suas finanças e aproveite todas as funcionalidades do <strong>Finito</strong>.
              </p>
              <div style="text-align: center; margin: 30px 0;">
                <p style="font-weight: bold; font-size: 20px; margin-top: 10px; color: #000;">O seu organizador financeiro</p>
              </div>
              <p style="font-size: 14px; color: #888; text-align: center;">
                Esta é uma mensagem automática. Não é necessário responder.
              </p>
              <p style="text-align: center; color: #2e7d32; font-weight: bold;">
                Equipe Finito
              </p>
            </div>
          </body>
        </html>
        """;

    @Override
    public void enviarEmail(String para, String assunto, String mensagem) {
        log.info("[start] EmailServiceImpl - enviarEmail");
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Finito <onboarding@resend.dev>")
                    .to(para)
                    .subject(assunto)
                    .html(HTML_TEMPLATE)
                    .build();

            CreateEmailResponse response = resend.emails().send(params);
            log.info("[finish] EmailServiceImpl - enviarEmail - id: {}", response.getId());

        } catch (Exception e) {
            log.error("[erro] EmailServiceImpl - enviarEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

    @Override
    public void enviarEmailHtml(@Email @NotNull String para, String assunto, String html) {
        log.info("[start] EmailServiceImpl - enviarEmailHtml");
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Finito <onboarding@resend.dev>")
                    .to(para)
                    .subject(assunto)
                    .html(html)
                    .build();

            resend.emails().send(params);
            log.info("[finish] EmailServiceImpl - enviarEmailHtml");

        } catch (Exception e) {
            log.error("[erro] EmailServiceImpl - enviarEmailHtml: {}", e.getMessage(), e);
        }
    }
}