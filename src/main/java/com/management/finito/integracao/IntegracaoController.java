package com.management.finito.integracao;

import com.management.finito.handler.APIException;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.infra.LancamentoJPARepository;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API de integração cross-app: expõe um resumo financeiro do usuário do Finito
 * para o TaskFlow (tela de Planejamento). PÚBLICA no filtro do JWT, protegida
 * pelo header X-Integration-Token == integracao.token.
 */
@RestController
@RequestMapping("/integracao")
@RequiredArgsConstructor
@Log4j2
public class IntegracaoController {
    private final PessoaRepository pessoaRepository;
    private final LancamentoJPARepository lancamentoRepository;

    @Value("${integracao.token:}")
    private String integracaoToken;

    private void autoriza(String token) {
        if (integracaoToken == null || integracaoToken.isBlank() || !integracaoToken.equals(token)) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Token de integração inválido.");
        }
    }

    /** Verifica se o e-mail existe no Finito (usado no passo "conectar"). */
    @GetMapping("/ping")
    public Map<String, Object> ping(@RequestHeader(value = "X-Integration-Token", required = false) String token,
                                    @RequestParam String email) {
        autoriza(token);
        Pessoa p = pessoaRepository.buscaEmail(email == null ? null : email.trim());
        return Map.of("existe", p != null, "nome", p != null ? p.getNomePessoa() : "");
    }

    /** Resumo financeiro mês a mês (12 meses) do ano informado. */
    @GetMapping("/resumo-anual")
    public ResumoAnualResponse resumoAnual(@RequestHeader(value = "X-Integration-Token", required = false) String token,
                                           @RequestParam String email, @RequestParam int ano) {
        autoriza(token);
        Pessoa pessoa = pessoaRepository.buscaEmail(email == null ? null : email.trim());
        if (pessoa == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "E-mail não encontrado no Finito.");
        }
        List<Lancamento> todos = lancamentoRepository.findAllByIdPessoaAndAno(pessoa.getIdPessoa(), ano);

        List<ResumoMesResponse> meses = new ArrayList<>(12);
        for (int mes = 1; mes <= 12; mes++) {
            double receitas = 0, despesas = 0, parcelasMetas = 0;
            for (Lancamento l : todos) {
                if (l.getMesDoLancamento() != mes) continue;
                double v = l.getPreco() == null ? 0 : l.getPreco();
                if (l.getTipo() == 1) {            // RECEITA
                    receitas += v;
                } else if (l.getTipo() == 2) {     // DESPESA
                    despesas += v;
                    if (l.getIdMeta() != 0) parcelasMetas += v;
                }
            }
            double saldo = receitas - despesas;
            meses.add(new ResumoMesResponse(mes, round2(receitas), round2(despesas), round2(saldo), round2(parcelasMetas), round2(saldo)));
        }
        return new ResumoAnualResponse(ano, meses);
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
