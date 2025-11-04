package com.management.finito.lancamento.application.service;

import com.management.finito.lancamento.application.api.*;
import com.management.finito.lancamento.application.repository.LancamentoRepository;
import com.management.finito.lancamento.domain.IdParcela;
import com.management.finito.lancamento.domain.IdRecorrencia;
import com.management.finito.lancamento.domain.Lancamento;
import com.management.finito.lancamento.domain.enums.MesDoLancamento;
import com.management.finito.lancamento.infra.ParcelaInfraJPARespository;
import com.management.finito.lancamento.infra.RecorrenciaInfraJPARespository;
import com.management.finito.meta.application.repository.MetaRepository;
import com.management.finito.meta.domain.Meta;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class LancamentoApplicationService implements LancamentoService {
    private final LancamentoRepository lancamentoRepository;
    private final PessoaRepository pessoaRepository;
    private final MetaRepository metaRepository;
    private final RecorrenciaInfraJPARespository recorrenciaInfraJPARespository;
    private final ParcelaInfraJPARespository parcelaInfraJPARespository;

    @Override
    public LancamentoResponse cadastraLancamento(LancamentoRequest lancamentoRequest, MesDoLancamento mes, int ano) {
        log.info("[start] LancamentoApplicationService - cadastraLancamento");
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Pega o Id do token
        pessoaRepository.buscaPessoaPorId(pessoa.getIdPessoa());
        Lancamento lancamentoCriado = new Lancamento(lancamentoRequest, pessoa.getIdPessoa(), mes, ano);

        Lancamento lancamentoSalvo = null;

        if (lancamentoRequest.getRecorrente()) {
            IdRecorrencia recorrencia = recorrenciaInfraJPARespository.save(new IdRecorrencia());
            lancamentoCriado.setIdRecorrencia(recorrencia.getId());
            lancamentoSalvo = lancamentoRepository.salva(lancamentoCriado);

            int proximoMes = mes.getId() + 1;

            for (int i = proximoMes; i <= 12; i++) {
                Lancamento lancamento = new Lancamento(lancamentoSalvo, i, recorrencia.getId());
                lancamentoRepository.salva(lancamento);
            }
        }else if (lancamentoRequest.getNumeroParcelas() > 1) {
            IdParcela idParcela = new IdParcela();
            parcelaInfraJPARespository.save(idParcela);
            lancamentoCriado.setIdParcela(idParcela.getId());

            int totalParcelas = lancamentoRequest.getNumeroParcelas();

            String descricao1 = lancamentoRequest.getDescricao() + " 1/" + totalParcelas ;
            lancamentoCriado.setDescricao(descricao1);
            lancamentoSalvo = lancamentoRepository.salva(lancamentoCriado);

            int mesaDD = lancamentoSalvo.getMesDoLancamento();
            int anoaDD = lancamentoSalvo.getAno();

            for (int i = 1; i < totalParcelas; i++) {
                mesaDD++;
                if (mesaDD > 12) {
                    mesaDD = 1;
                    anoaDD++;
                }

                // int j = 1+i
                String descricao = lancamentoRequest.getDescricao() +" "+ (1+i) + "/" + totalParcelas ;

                Lancamento novo = new Lancamento(lancamentoSalvo, mesaDD, anoaDD, descricao);
                lancamentoRepository.salva(novo);
            }
        } else {
            lancamentoSalvo = lancamentoRepository.salva(lancamentoCriado);
        }
        log.info("[finish] LancamentoApplicationService - cadastraLancamento");
        return new LancamentoResponse(lancamentoSalvo);
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
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamentoRepository.deletaLancamento(idLancamento);
        log.info("[finish] LancamentoApplicationService - deletaLancamento");
    }

    @Override
    public void editaLancamento(UUID idLancamento, LancamentoAlteracaoRequest lancamentoAlteracaoRequest) {
        log.info("[start] LancamentoApplicationService - editaLancamento");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.atualiza(lancamentoAlteracaoRequest);
        //adicionar mudança no total das metas
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - editaLancamento");
    }

    @Override
    public void mudaStatusParaPendente(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - mudaStatusParaPendente");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.mudaStatusParaPendente();
        if(lancamento.getIdMeta() != 0){
            Meta meta = metaRepository.getMetaId(lancamento.getIdMeta());
            meta.removeParcelaPaga();
            metaRepository.saveMeta(meta);
        }
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - mudaStatusParaPendente");
    }

    @Override
    public void mudaStatusParaPago(UUID idLancamento) {
        log.info("[start] LancamentoApplicationService - mudaStatusParaPago");
        Lancamento lancamento = lancamentoRepository.buscaLancamento(idLancamento);
        lancamento.mudaStatusParaPago();
        if(lancamento.getIdMeta() != 0){
            Meta meta = metaRepository.getMetaId(lancamento.getIdMeta());
            meta.addParcelaPaga();
            metaRepository.saveMeta(meta);
        }
        lancamentoRepository.salva(lancamento);
        log.info("[finish] LancamentoApplicationService - mudaStatusParaPago");
    }

    @Override
    public void replicaLancamentos(ReplicaLancamentosRequest replicaLancamentosRequest) {
        log.info("[start] LancamentoApplicationService - replicaLancamentos");
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Lancamento> lancamentosOriginais = lancamentoRepository.buscaLancamentosPorIds(replicaLancamentosRequest.getIdsLancamentos());


        List<Lancamento> novosLancamentos = lancamentosOriginais.stream()
                .map(l -> {
                    Lancamento novo = new Lancamento(l); // Construtor copia dados do original
                    novo.setMesDoLancamento(replicaLancamentosRequest.getMesDestino());
                    novo.setAno(replicaLancamentosRequest.getAnoDestino());

                    // Ajusta data de vencimento mantendo o dia, mas corrigindo se for inválido
                    int anoDestino = replicaLancamentosRequest.getAnoDestino();
                    int mesDestino = replicaLancamentosRequest.getMesDestino();
                    int diaOriginal = l.getDataVencimento().getDayOfMonth();
                    YearMonth yearMonthDestino = YearMonth.of(anoDestino, mesDestino);
                    int ultimoDiaDoMes = yearMonthDestino.lengthOfMonth();
                    int novoDia = Math.min(diaOriginal, ultimoDiaDoMes);

                    LocalDate novaDataVencimento = LocalDate.of(anoDestino, mesDestino, novoDia);
                    novo.setDataVencimento(novaDataVencimento);
                    return novo;
                })
                .collect(Collectors.toList());
        lancamentoRepository.savaTodosLancamentos(novosLancamentos);
        log.info("[finish] LancamentoApplicationService - replicaLancamentos");
    }

    @Override
    public void cadastraLancamentoEmLote(List<@Valid LancamentoEmLoteRequest> lancamentosEmLoteRequest, int idMeta) {
        log.info("[start] LancamentoApplicationService - cadastraLancamentoEmLote");
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Pega o Id do token
        pessoaRepository.buscaPessoaPorId(pessoa.getIdPessoa());
        Meta meta = metaRepository.getMetaId(idMeta);

        //Cria os lancamentos em Lote e salva
        List<Lancamento> lancamentos = Lancamento.criaLancamentosEmLote(lancamentosEmLoteRequest, idMeta, pessoa.getIdPessoa());
        lancamentoRepository.savaTodosLancamentos(lancamentos);

        //Altera e Salva ParcelasTotais da Meta
        meta.alteraParcelasTotais(lancamentos);
        metaRepository.saveMeta(meta);
        log.info("[finish] LancamentoApplicationService - cadastraLancamentoEmLote");
    }

    @Override
    public void deletaAllLancamentoRecorrente(int idRecorrencia, LocalDate data) {
        log.info("[start] LancamentoApplicationService - deletaAllLancamentoRecorrente");
        List<Lancamento> lancamentos = lancamentoRepository.buscaLancamentoIdRecorrencia(idRecorrencia);
        List<Lancamento> lancamentosParaExcluir = lancamentos.stream()
                .filter(l -> !l.getDataVencimento().isBefore(data))
                .toList();
        lancamentoRepository.deleteAllLancamentosRecorrencia(lancamentosParaExcluir);
        log.info("[finish] LancamentoApplicationService - deletaAllLancamentoRecorrente");
    }

    @Override
    public void deletaAllLancamentoParcelado(int idParcela, LocalDate dataVencimento) {
        log.info("[start] LancamentoApplicationService - deletaAllLancamentoParcelado");
        List<Lancamento> lancamentos = lancamentoRepository.buscaLancamentoIdParcela(idParcela);
        List<Lancamento> lancamentosParaExcluir = lancamentos.stream()
                .filter(l -> !l.getDataVencimento().isBefore(dataVencimento))
                .toList();
        lancamentoRepository.deleteAllLancamentosRecorrencia(lancamentosParaExcluir);
        log.info("[finish] LancamentoApplicationService - deletaAllLancamentoParcelado");
    }
}