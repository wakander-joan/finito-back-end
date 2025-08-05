package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.LancamentoAlteracaoRequest;
import com.management.finito.lancamento.application.api.LancamentoDetalhadoResponse;
import com.management.finito.lancamento.application.api.LancamentoRequest;
import com.management.finito.lancamento.application.api.LancamentoResponse;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Service
public class LancamentoApplicationService implements LancamentoService {
    private final LancamentoRepository lancamentoRepository;
    private final PessoaRepository pessoaRepository;

    @Override
    public LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest, MesDoLancamento mes, int ano) {
        log.info("[start] LancamentoApplicationService - cadastraLancamento");
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Pega o Id do token
        pessoaRepository.buscaPessoaPorId(pessoa.getIdPessoa());
        Lancamento lancamentoCriado = lancamentoRepository.salva(new Lancamento(lancamentoRequest, pessoa.getIdPessoa(),mes, ano));
        log.info("[finish] LancamentoApplicationService - cadastraLancamento");
        return new LancamentoResponse(lancamentoCriado);
    }

    @Override
    public LancamentoDetalhadoResponse buscaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - buscaLancamento");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        log.info("[finish] LancamentoApplicationService - buscaLancamento");
        return new LancamentoDetalhadoResponse(lancamento);
    }

    @Override
    public List<LancamentoDetalhadoResponse> buscaLancamentosPorMesEAno(MesDoLancamento mes, int ano) {
        log.info("[start] LancamentoApplicationService - buscaTodosLancamentoPorMes");
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Lancamento> lancamentos = lancamentoRepository.buscaLancamentosPorMesEAno(mes, pessoa.getIdPessoa(), ano);
        log.info("[finish] LancamentoApplicationService - buscaTodosLancamentoPorMes");
        return LancamentoDetalhadoResponse.converte(lancamentos);
    }

    @Override
    public void deletaLancamento(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - deletaLancamento");
        lancamentoRepository.buscaLancamento(idLancamento);
        lancamentoRepository.deletaLancamento(idLancamento);
        log.info("[finish] LancamentoApplicationService - deletaLancamento");
    }

    @Override
    public void editaLancamento(UUID idLancamento, LancamentoAlteracaoRequest lancamentoAlteracaoRequest) {
        log.info("[start] LancamentoApplicationService - editaLancamento");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.atualiza(lancamentoAlteracaoRequest);
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - editaLancamento");
    }

    @Override
    public void mudaStatusParaPendente(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - mudaStatusParaPendente");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.mudaStatusParaPendente();
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - mudaStatusParaPendente");
    }

    @Override
    public void mudaStatusParaPago(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - mudaStatusParaPago");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.mudaStatusParaPago();
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - mudaStatusParaPago");
    }
}
