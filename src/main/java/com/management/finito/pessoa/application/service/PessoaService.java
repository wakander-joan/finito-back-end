package com.management.finito.pessoa.application.service;

import com.management.finito.pessoa.application.api.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;



public interface PessoaService {
	PessoaResponse criarPessoa(@Valid PessoaRequest pessoaRequeste);
	PessoaDetalhadaResponse buscaPessoaPorId(UUID idPessoa);
	List<PessoaListResponse> buscaTodasPessoas();
	void deletaPessoaPorId(UUID idPessoa);
	void editaPessoaPorId(UUID idPessoa, @Valid PessoaAlteracaoRequest pessoaAlteracaoRequest);
}
