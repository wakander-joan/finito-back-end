package com.management.finito.pessoa.application.api;

import java.util.List;
import java.util.UUID;


import com.management.finito.pessoa.application.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PessoaController implements PessoaAPI {
	private final PessoaService pessoaService;

	@Override
	public PessoaResponse criaPessoa(@Valid PessoaRequest pessoaRequeste) {
		log.info("[inicia] PessoaController - criaPessoa");
		PessoaResponse pessoa = pessoaService.criarPessoa(pessoaRequeste);
		log.info("[finaliza] PessoaController - criaPessoa");
		return pessoa;
	}

	@Override
	public PessoaDetalhadaResponse buscaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaController - buscaPessoaAtravesId");
		PessoaDetalhadaResponse pessoa = pessoaService.buscaPessoaPorId(idPessoa);
		log.info("[finaliza] PessoaController - buscaPessoaAtravesId");
		return pessoa;
	}

	@Override
	public List<PessoaListResponse> buscarTodasPessoas() {
		log.info("[inicia] PessoaController - buscarTodasPessoas");
		List<PessoaListResponse> lista = pessoaService.buscaTodasPessoas();
		log.info("[finaliza] PessoaController - buscarTodasPessoas");
		return lista;
	}

	@Override
	public void deletaPessoaPorId(UUID idPessoa) {
		log.info("[inicia] PessoaController - deletaPessoaPorId");
		pessoaService.deletaPessoaPorId(idPessoa);
		log.info("[finaliza] PessoaController - deletaPessoaPorId");
	}

	@Override
	public void editaPessoaPorId(UUID idPessoa, @Valid PessoaAlteracaoRequest pessoaAlteracaoRequest) {
		log.info("[inicia] PessoaController - editaPessoaPorId");
		pessoaService.editaPessoaPorId(idPessoa, pessoaAlteracaoRequest);
		log.info("[finaliza] PessoaController - editaPessoaPorId");
	}
}
