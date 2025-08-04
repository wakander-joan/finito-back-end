package com.management.finito.pessoa.application.service;

import java.util.List;
import java.util.UUID;


import com.management.finito.handler.APIException;
import com.management.finito.pessoa.application.api.*;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PessoaApplicationService implements PessoaService {
	private final PessoaRepository pessoaRepository;

	@Override
	public PessoaResponse criarPessoa(@Valid PessoaRequest pessoaRequeste) {
		log.info("[inicia] PessoaApplicationService - criaPessoa");
		validaEmailCadastrado(pessoaRequeste.getEmail());
		Pessoa pessoa = pessoaRepository.salva(new Pessoa(pessoaRequeste));
		log.info("[finaliza] PessoaApplicationService - criaPessoa");
		return PessoaResponse.builder().idPessoa(pessoa.getIdPessoa()).build();
	}

	private void validaEmailCadastrado(@Email @NotNull String email) {
		log.info("[start] PessoaApplicationService - validaEmailCadastrado");
		Pessoa pessoa = pessoaRepository.buscaEmail(email);
		if (pessoa != null){
			throw APIException.build(HttpStatus.NOT_FOUND, "Email já cadastrado!");
		}
		log.info("[Email Verificado!]");
		log.info("[finish] PessoaApplicationService - validaEmailCadastrado");
	}

	@Override
	public PessoaDetalhadaResponse buscaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaApplicationService - buscaPessoa");
		Pessoa pessoa = pessoaRepository.buscaPessoaPorId(idPessoa);
		log.info("[finaliza] PessoaApplicationService - buscaPessoa");
		return new PessoaDetalhadaResponse(pessoa);
	}

	@Override
	public List<PessoaListResponse> buscaTodasPessoas() {
		log.info("[inicia] PessoaApplicationService - buscaTodasPessoas");
		List<Pessoa> pessoas = pessoaRepository.buscaTodasPessoas();
		log.info("[finaliza] PessoaApplicationService - buscaTodasPessoas");
		return PessoaListResponse.converte(pessoas);
	}

	@Override
	public void deletaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaApplicationService - deletaPessoaPorId");
		pessoaRepository.buscaPessoaPorId(idPessoa);
		pessoaRepository.deletaPessoaPorId(idPessoa);
		log.info("[finaliza] PessoaApplicationService - deletaPessoaPorId");
	}

	@Override
	public void editaPessoaPorId(UUID idPessoa, @Valid PessoaAlteracaoRequest pessoaAlteracaoRequest) {
		log.info("[inicia] PessoaApplicationService - editaPessoaPorId");
		Pessoa pessoa = pessoaRepository.buscaPessoaPorId(idPessoa);
		pessoa.edita(pessoaAlteracaoRequest);
		pessoaRepository.salva(pessoa);
		log.info("[finaliza] PessoaApplicationService - editaPessoaPorId");
	}
}
