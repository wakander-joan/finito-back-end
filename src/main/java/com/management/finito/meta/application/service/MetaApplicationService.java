package com.management.finito.meta.application.service;

import com.management.finito.meta.application.api.MetaDetalhadaResponse;
import com.management.finito.meta.application.api.MetaRequest;
import com.management.finito.meta.application.api.MetaResponse;
import com.management.finito.meta.application.repository.EtapaRepository;
import com.management.finito.meta.application.repository.MetasRepository;
import com.management.finito.meta.domain.Etapa;
import com.management.finito.meta.domain.Meta;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<MetaDetalhadaResponse> buscaMeta() {
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
}
