package com.management.finito.pessoa.application.repository;

import com.management.finito.pessoa.domain.Pessoa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;


public interface PessoaRepository {
	Pessoa salva(Pessoa pessoa);
	Pessoa buscaPessoaPorId(UUID idPessoa);
	List<Pessoa> buscaTodasPessoas();
	void deletaPessoaPorId(UUID idPessoa);
    Pessoa buscaEmail(@Email @NotNull String email);
}
