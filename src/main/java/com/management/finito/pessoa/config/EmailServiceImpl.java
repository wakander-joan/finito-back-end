package com.management.finito.pessoa.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

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
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("Finito <" + remetente + ">");
            email.setTo(para);
            email.setSubject(assunto);
            email.setText(HTML_TEMPLATE);
            mailSender.send(email);
            log.info("[finish] EmailServiceImpl - enviarEmail");
        } catch (Exception e) {
            log.error("[erro] EmailServiceImpl - enviarEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

    // Template de boas-vindas com a identidade visual do Finito (tema escuro, logo F rosa).
    private static final String BOAS_VINDAS_HTML = """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0"></head>
        <body style="margin:0;padding:0;background-color:#0a0d14;">
          <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#0a0d14;font-family:'Segoe UI',Arial,sans-serif;">
            <tr><td align="center" style="padding:32px 14px;">
              <table role="presentation" width="520" cellpadding="0" cellspacing="0" style="width:520px;max-width:100%;background-color:#12151f;border:1px solid rgba(255,255,255,0.08);border-radius:18px;">
                <tr><td style="padding:34px 36px 10px;">
                  <table role="presentation" cellpadding="0" cellspacing="0"><tr>
                    <td style="width:46px;height:46px;background-color:#e11d48;background-image:linear-gradient(135deg,#ff6b81,#e11d48);border-radius:13px;color:#ffffff;font-size:27px;font-weight:800;text-align:center;font-family:Arial,sans-serif;">F</td>
                    <td style="padding-left:12px;color:#ffffff;font-size:23px;font-weight:800;letter-spacing:1.5px;">FINITO</td>
                  </tr></table>
                </td></tr>
                <tr><td style="padding:18px 36px 6px;">
                  <h1 style="margin:0 0 10px;color:#ffffff;font-size:22px;font-weight:800;">{{SAUDACAO}} &#127881;</h1>
                  <p style="margin:0;color:#aab3c5;font-size:15px;line-height:1.65;">Seu cadastro no <span style="color:#ff6b81;font-weight:700;">Finito</span> foi realizado com sucesso. Agora e so comecar: lance receitas e despesas, defina metas, monte sua reserva e acompanhe tudo em graficos.</p>
                </td></tr>
                <tr><td align="center" style="padding:26px 36px 6px;">
                  <a href="https://finito-production.up.railway.app" style="display:inline-block;background-color:#e11d48;background-image:linear-gradient(135deg,#ff3b5c,#e11d48);color:#ffffff;text-decoration:none;font-size:15px;font-weight:700;padding:14px 30px;border-radius:999px;">Acessar o Finito &#8594;</a>
                </td></tr>
                <tr><td align="center" style="padding:22px 36px 8px;">
                  <p style="margin:0;color:#6b7488;font-size:13px;">&#128184; O seu organizador financeiro</p>
                </td></tr>
                <tr><td style="padding:18px 36px 34px;border-top:1px solid rgba(255,255,255,0.07);">
                  <p style="margin:14px 0 0;color:#6b7488;font-size:12px;text-align:center;">Esta e uma mensagem automatica, nao e necessario responder.</p>
                  <p style="margin:6px 0 0;color:#ff6b81;font-size:12px;font-weight:700;text-align:center;">Equipe Finito</p>
                </td></tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """;

    @Async
    @Override
    public void enviarBoasVindas(String para, String nome) {
        String primeiro = (nome == null || nome.isBlank()) ? "" : nome.trim().split("\\s+")[0];
        String saudacao = primeiro.isEmpty() ? "Bem-vindo(a)!" : "Bem-vindo(a), " + primeiro + "!";
        String html = BOAS_VINDAS_HTML.replace("{{SAUDACAO}}", saudacao);
        enviarEmailHtml(para, "Bem-vindo(a) ao Finito!", html);
    }

    @Override
    public void enviarEmailHtml(@Email @NotNull String para, String assunto, String html) {
        log.info("[start] EmailServiceImpl - enviarEmailHtml");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("Finito <" + remetente + ">");
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("[finish] EmailServiceImpl - enviarEmailHtml");
        } catch (Exception e) {
            // Captura tambem MailSendException (runtime) do Spring, nao so MessagingException.
            log.error("[erro] EmailServiceImpl - enviarEmailHtml: {}", e.getMessage(), e);
        }
    }
}
