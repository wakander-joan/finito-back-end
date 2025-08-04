package com.management.finito.pessoa.domain;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import com.management.finito.pessoa.application.api.PessoaAlteracaoRequest;
import com.management.finito.pessoa.application.api.PessoaRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Pessoa {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", name = "id", updatable = false, unique = true, nullable = false)
	private UUID idPessoa;
	@NotBlank
	private String nomePessoa;
	@Email
	@NotNull
	@Column(unique = true, nullable = false)
	private String email;
	@JsonIgnore
	@Column(nullable = false)
	private String senha;

	public Pessoa(PessoaRequest pessoaRequest) {
		this.nomePessoa = pessoaRequest.getNomePessoa();
		this.email = pessoaRequest.getEmail();
		this.senha = new BCryptPasswordEncoder().encode(pessoaRequest.getSenha());
	}

	public void edita(@Valid PessoaAlteracaoRequest pessoaAlteracaoRequest) {
		this.nomePessoa = pessoaAlteracaoRequest.getNomePessoa();
		this.email = pessoaAlteracaoRequest.getEmail();
	}
}