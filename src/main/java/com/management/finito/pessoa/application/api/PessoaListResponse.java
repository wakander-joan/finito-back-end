package com.management.finito.pessoa.application.api;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import com.management.finito.pessoa.domain.Pessoa;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PessoaListResponse {
	private UUID idPessoa;
	private String nomePessoa;
	private String email;

	public static List<PessoaListResponse> converte(List<Pessoa> pessoa) {
		return pessoa.stream().map(c -> new PessoaListResponse(c)).collect(Collectors.toList());
	}

	public PessoaListResponse(Pessoa pessoa) {
		super();
		this.idPessoa = pessoa.getIdPessoa();
		this.nomePessoa = pessoa.getNomePessoa();
		this.email = pessoa.getEmail();
	}
}
