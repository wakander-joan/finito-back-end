package com.management.finito.pessoa.infra;

import java.util.List;
import java.util.UUID;

import com.management.finito.handler.APIException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;


import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Repository
@Log4j2
@RequiredArgsConstructor
public class PessoaInfraRepository implements PessoaRepository {
	private final PessoaSpringDataJPARepository pessoaSpringDataJPARepository;

	@Override
	public Pessoa salva(Pessoa pessoa) {
		log.info("[inicia] PessoaInfraRepository - salva");
		try {
			pessoaSpringDataJPARepository.save(pessoa);
		}  catch (Exception e) {
			log.error("Erro ao salvar pessoa: ", e);
			throw APIException.build(HttpStatus.NOT_FOUND, "Não foi possível cadastrar o cliente.");
		} finally {
			log.info("[finaliza] PessoaInfraRepository - salva");
		}
		return pessoa;
	}

	@Override
	public Pessoa buscaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaInfraRepository - buscaPessoa");
		Pessoa pessoa = pessoaSpringDataJPARepository.findById(idPessoa)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
		log.info("[finaliza] PessoaInfraRepository - buscaPessoa");
		return pessoa;
	}

	@Override
	public List<Pessoa> buscaTodasPessoas() {
		log.info("[inicia] PessoaInfraRepository - buscaTodasPessoas");
		List<Pessoa> todasPessoas = pessoaSpringDataJPARepository.findAll();
		log.info("[finaliza] PessoaInfraRepository - buscaTodasPessoas");
		return todasPessoas;
	}

	@Override
	public void deletaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaInfraRepository - deletaPessoaPorId");
		pessoaSpringDataJPARepository.deleteById(idPessoa);
		log.info("[finaliza] PessoaInfraRepository - deletaPessoaPorId");
	}

	@Override
	public Pessoa buscaEmail(String email) {
		log.info("[start] PessoaInfraRepository - buscaEmail");
		Pessoa pessoa = pessoaSpringDataJPARepository.findByEmail(email);
		log.info("[finish] PessoaInfraRepository - buscaEmail");
		return pessoa;
	}
}