package com.management.finito.config;

import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
import com.management.finito.lancamento.domain.enums.TipoLancamento;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.config.EmailService;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificacaoVencimentoScheduler {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LancamentoRepository lancamentoRepository;
    private final PessoaRepository pessoaRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *") // Executa todo dia às 08:00 da manhã
    public void notificarVencimentos() {
        log.info("[start] NotificacaoVencimentoScheduler - notificarVencimentos");

        LocalDate hoje = LocalDate.now();
        notificarLancamentos(hoje.plusDays(3), 3); // faltam 3 dias para vencer
        notificarLancamentos(hoje, 0);             // vence hoje

        log.info("[end] NotificacaoVencimentoScheduler - notificarVencimentos");
    }

    // Notifica os lançamentos (Receita ou Despesa) PENDENTES que vencem na data informada.
    private void notificarLancamentos(LocalDate data, int diasRestantes) {
        List<Lancamento> lancamentos = lancamentoRepository.buscaVencimentosPendentesNaData(data);

        for (Lancamento lancamento : lancamentos) {
            Pessoa pessoa = pessoaRepository.buscaPessoaPorId(lancamento.getIdPessoa());
            boolean receita = lancamento.getTipo() == TipoLancamento.RECEITA.getId();

            String assunto = montarAssunto(lancamento, receita, diasRestantes);
            String mensagem = criarMensagemHtml(lancamento, pessoa, receita, diasRestantes);

            emailService.enviarEmailHtml(pessoa.getEmail(), assunto, mensagem);
        }
    }

    private String montarAssunto(Lancamento lancamento, boolean receita, int diasRestantes) {
        String quando = diasRestantes == 0 ? "hoje" : "em " + diasRestantes + " dias";
        String rotulo = receita ? "Recebimento" : "Vencimento";
        return rotulo + " " + quando + ": " + lancamento.getDescricao() + " - Finito";
    }

    private String criarMensagemHtml(Lancamento lancamento, Pessoa pessoa, boolean receita, int diasRestantes) {
        String destaque = receita ? "#22c55e" : "#ff6b81"; // verde p/ receita, rosa p/ despesa
        String quando = diasRestantes == 0 ? "hoje" : "daqui a " + diasRestantes + " dias";

        String titulo = receita
                ? (diasRestantes == 0 ? "Recebimento previsto para hoje" : "Recebimento em " + diasRestantes + " dias")
                : (diasRestantes == 0 ? "Vencimento hoje" : "Vence em " + diasRestantes + " dias");

        String frase = receita
                ? "Este é um lembrete de que a receita abaixo está prevista para " + quando + "."
                : "Este é um lembrete de que a despesa abaixo vence " + quando + ".";

        String rotuloData = receita ? "Recebimento" : "Vencimento";

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0"></head>
            <body style="margin:0;padding:0;background-color:#0a0d14;">
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#0a0d14;font-family:'Segoe UI',Arial,sans-serif;">
                <tr><td align="center" style="padding:32px 14px;">
                  <table role="presentation" width="520" cellpadding="0" cellspacing="0" style="width:520px;max-width:100%%;background-color:#12151f;border:1px solid rgba(255,255,255,0.08);border-radius:18px;">
                    <tr><td style="padding:34px 36px 10px;">
                      <table role="presentation" cellpadding="0" cellspacing="0"><tr>
                        <td style="width:46px;height:46px;background-color:%1$s;border-radius:13px;color:#ffffff;font-size:27px;font-weight:800;text-align:center;font-family:Arial,sans-serif;">F</td>
                        <td style="padding-left:12px;color:#ffffff;font-size:23px;font-weight:800;letter-spacing:1.5px;">FINITO</td>
                      </tr></table>
                    </td></tr>
                    <tr><td style="padding:18px 36px 6px;">
                      <h1 style="margin:0 0 10px;color:#ffffff;font-size:22px;font-weight:800;">%2$s</h1>
                      <p style="margin:0 0 4px;color:#aab3c5;font-size:15px;">Olá <strong style="color:#ffffff;">%3$s</strong>,</p>
                      <p style="margin:0 0 16px;color:#aab3c5;font-size:15px;line-height:1.65;">%4$s</p>
                    </td></tr>
                    <tr><td style="padding:6px 36px 6px;">
                      <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#0a0d14;border:1px solid rgba(255,255,255,0.08);border-radius:12px;">
                        <tr><td style="padding:16px 20px;">
                          <p style="margin:0 0 6px;color:#ffffff;font-size:17px;font-weight:700;">%5$s</p>
                          <p style="margin:0;color:%1$s;font-size:22px;font-weight:800;">R$ %6$.2f</p>
                          <p style="margin:10px 0 0;color:#aab3c5;font-size:14px;">%7$s: <strong style="color:#ffffff;">%8$s</strong></p>
                          <p style="margin:4px 0 0;color:#aab3c5;font-size:14px;">Categoria: <strong style="color:#ffffff;">%9$s</strong></p>
                        </td></tr>
                      </table>
                    </td></tr>
                    <tr><td align="center" style="padding:24px 36px 6px;">
                      <a href="https://finito-production.up.railway.app" style="display:inline-block;background-color:%1$s;color:#ffffff;text-decoration:none;font-size:15px;font-weight:700;padding:14px 30px;border-radius:999px;">Abrir o Finito &#8594;</a>
                    </td></tr>
                    <tr><td style="padding:18px 36px 34px;border-top:1px solid rgba(255,255,255,0.07);">
                      <p style="margin:14px 0 0;color:#6b7488;font-size:12px;text-align:center;">Esta é uma mensagem automática, não é necessário responder.</p>
                      <p style="margin:6px 0 0;color:%1$s;font-size:12px;font-weight:700;text-align:center;">Equipe Finito</p>
                    </td></tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(
                destaque,                                                       // %1$s cor de destaque
                titulo,                                                         // %2$s título (h1)
                pessoa.getNomePessoa(),                                         // %3$s nome
                frase,                                                          // %4$s frase de contexto
                lancamento.getDescricao(),                                      // %5$s descrição
                lancamento.getPreco(),                                          // %6$.2f valor
                rotuloData,                                                     // %7$s rótulo da data (Vencimento/Recebimento)
                lancamento.getDataVencimento().format(DATA_BR),                 // %8$s data
                CategoriaLancamento.fromId(lancamento.getCategoriaLancamento()) // %9$s categoria
        );
    }
}
