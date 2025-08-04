package com.management.finito.pessoa.config;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void enviarEmail(String para, String assunto, String mensagemHtml) {
        log.info("[start] EmailServiceImpl - enviarEmail");

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);

            String htmlComImagem = """
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
                    <img src='cid:logoFinito' alt='Logo Finito' style="width: 100px;"/>
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

            helper.setText(htmlComImagem, true);

            File imagem = new File("src/main/resources/static/images/finito-logo.png");
            helper.addInline("logoFinito", imagem);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("[erro] EmailServiceImpl - enviarEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
        log.info("[finish] EmailServiceImpl - enviarEmail");
    }
}
