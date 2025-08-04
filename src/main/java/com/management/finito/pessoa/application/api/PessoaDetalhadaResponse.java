package com.management.finito.pessoa.application.api;

import com.management.finito.pessoa.domain.Pessoa;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;


import java.time.LocalDate;
import java.util.UUID;



@Getter
@ToString
public class PessoaDetalhadaResponse {
	private UUID idPessoa;
	private String nomePessoa;
	private String email;

	public PessoaDetalhadaResponse(Pessoa pessoa) {
		this.idPessoa = pessoa.getIdPessoa();
		this.nomePessoa = pessoa.getNomePessoa();
		this.email = pessoa.getEmail();
	}
}
