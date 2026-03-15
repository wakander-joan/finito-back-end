package com.management.finito.meta.application.service;

import com.management.finito.handler.APIException;
import com.management.finito.meta.application.api.MetaDetalhadaResponse;
import com.management.finito.meta.application.api.MetaRequest;
import com.management.finito.meta.application.api.MetaResponse;
import com.management.finito.meta.application.repository.EtapaRepository;
import com.management.finito.meta.application.repository.MetasRepository;
import com.management.finito.meta.domain.Etapa;
import com.management.finito.meta.domain.Meta;
import com.management.finito.meta.domain.StatusEtapa;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Service
public class MetaApplicationService implements MetaService {
    private final PessoaRepository pessoaRepository;
    private final MetasRepository metaRepository;
    private final EtapaRepository etapaRepository;

    @Override
    public MetaResponse cadastraMeta(MetaRequest metatoRequest) {
        log.info("[start] MetaApplicationService - cadastraMeta");
        ArrayList<Etapa> etapasParaSalvar = new ArrayList<>();

        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Pega o Id do token
        pessoaRepository.buscaPessoaPorId(pessoa.getIdPessoa());

        Meta metaCriada = new Meta(metatoRequest, pessoa.getIdPessoa());
        Meta metaSalva =  metaRepository.salvaMeta(metaCriada);

        metatoRequest.getEtapas().forEach(c -> {
            log.info("UUID da Meta: "+ metaSalva.getIdMeta());
            Etapa etapa = new Etapa(c, metaSalva.getIdMeta());
            metaSalva.setTotalEtapas(metaSalva.getTotalEtapas()+1);
            etapasParaSalvar.add(etapa);
        });

        //Falta implementar os dois!....................................................................................
        metaRepository.salvaMeta(metaSalva);
        etapaRepository.salvaEtapas(etapasParaSalvar);

        log.info("[finish] MetaApplicationService - cadastraMeta");
        return new MetaResponse(metaSalva);
    }

    @Override
    public List<MetaDetalhadaResponse> buscaMetas() {
        log.info("[start] MetaApplicationService - buscaMeta");
        //Valida e pega o Id do Usuario
        Pessoa pessoa = (Pessoa) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Pega o Id do token
        pessoaRepository.buscaPessoaPorId(pessoa.getIdPessoa());
        //Busca as metas
        List<Meta> metasBuscada = metaRepository.buscaMetas(pessoa.getIdPessoa());

        List<MetaDetalhadaResponse> listaDeMetasDetalhadas = new ArrayList<>();
        //Cria response
        metasBuscada.forEach(c -> {
            ArrayList<Etapa> etapasBuscadas = etapaRepository.buscaEtapas(c.getIdMeta());
            MetaDetalhadaResponse metaCriada = new MetaDetalhadaResponse(c, etapasBuscadas);
            listaDeMetasDetalhadas.add(metaCriada);
        });
        log.info("[finish] MetaApplicationService - buscaMeta");
        return listaDeMetasDetalhadas;
    }

    @Override
    public void deletaMeta(UUID idMeta) {
        log.info("[start] MetaApplicationService - deletaMeta");
        Meta metaBuscada = metaRepository.buscaMeta(idMeta);
        ArrayList<Etapa> etapasBuscadas = etapaRepository.buscaEtapas(metaBuscada.getIdMeta());

        //Deleta as etapas
        etapasBuscadas.forEach(c -> {
            etapaRepository.deletaEtapa(c.getIdEtapa());
        });

        //Segunda verificação!
        ArrayList<Etapa> etapasBuscadasDenovo = etapaRepository.buscaEtapas(metaBuscada.getIdMeta());
        if(etapasBuscadasDenovo.isEmpty()){
            metaRepository.deletaMeta(metaBuscada.getIdMeta());
            log.info("###  Meta deletada com sucesso!!");
        }else {
            throw APIException.build(HttpStatus.NOT_FOUND, "Não é possivel deletar a Meta pois ainda tem Etapas");
        }
        log.info("[finish] MetaApplicationService - deletaMeta");
    }

    @Override
    public void alteraStatusEtapa(UUID idEtapa, String status) {
        log.info("[start] MetaApplicationService - alteraStatusEtapa");
        Etapa etapaBuscada = etapaRepository.buscaEtapa(idEtapa);
        log.info("status={}", status);
        log.info(
                "Alterando status da etapa | idEtapa={} | para={}",
                etapaBuscada.getIdEtapa(),
                status
        );
        StatusEtapa statusEnum = StatusEtapa.valueOf(status.toUpperCase());
        Meta meta = metaRepository.buscaMeta(etapaBuscada.getIdMeta());

        log.info("Entrou nas validações da META");
        if (statusEnum.equals(StatusEtapa.CONCLUIDA) && etapaBuscada.getStatus() != StatusEtapa.CONCLUIDA) {
            meta.addEtapaConcluida();
        }
        log.info("Status estapa Banco {}", etapaBuscada.getStatus());
        if (etapaBuscada.getStatus().equals(StatusEtapa.CONCLUIDA) && statusEnum != StatusEtapa.CONCLUIDA){
            meta.removeEtapaConcluida();
        }
        log.info("Saiu nas validações da META");

        etapaBuscada.alteraStatusEtapa(statusEnum);
        etapaRepository.salvaEtapa(etapaBuscada);
        metaRepository.salvaMeta(meta);
        log.info("[finish] MetaApplicationService - alteraStatusEtapa");
    }
}