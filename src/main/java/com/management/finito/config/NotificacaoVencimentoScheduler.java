package com.management.finito.config;

import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.CategoriaLancamento;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificacaoVencimentoScheduler {

    private final LancamentoRepository lancamentoRepository;
    private final PessoaRepository pessoaRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *") // Executa todo dia às 08:00 da manhã
    public void notificarVencimentosDoDia() {
        log.info("[start] NotificacaoVencimentoScheduler - notificarVencimentosDoDia");

        LocalDate hoje = LocalDate.now();

        List<Lancamento> vencendoHoje = lancamentoRepository.findByDataVencimento(hoje);

        for (Lancamento lancamento : vencendoHoje) {
            Pessoa pessoa = pessoaRepository.buscaPessoaPorId(lancamento.getIdPessoa());

            String assunto = "Lembrete de Vencimento - Finito";
            String mensagem = criarMensagemHtml(lancamento, pessoa);

            emailService.enviarEmailHtml(pessoa.getEmail(), assunto, mensagem);
        }

        log.info("[end] NotificacaoVencimentoScheduler - notificarVencimentosDoDia");
    }

    private String criarMensagemHtml(Lancamento lancamento, Pessoa pessoa) {
        return """
            <html>
              <body style="font-family: Arial, sans-serif; padding: 20px; background-color: #f7f7f7;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                  <h2 style="color: #d32f2f;">Lembrete de Vencimento</h2>
                  <p>Olá %s,</p>
                  <p>Estamos te lembrando que o lançamento <strong>"%s"</strong> vence hoje: <strong>%s</strong>.</p>
                  <p>Valor: <strong>R$ %.2f</strong></p>
                  <p>Categoria: %s</p>
                  <hr style="margin: 20px 0;" />
                  <p style="color: #888;">Essa é uma mensagem automática enviada pelo sistema Finito.</p>
                  <p style="text-align: center; font-weight: bold; color: #388e3c;">Equipe Finito</p>
                </div>
              </body>
            </html>
            """.formatted(
                pessoa.getNomePessoa(),
                lancamento.getDescricao(),
                lancamento.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                lancamento.getPreco(),
                CategoriaLancamento.fromId(lancamento.getCategoriaLancamento())

        );
    }
}
