package com.management.finito.pessoa.application.api;

import java.time.LocalDate;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Value;

@Getter
@ToString
@Builder
public class PessoaRequest {
	@NotBlank
	private String nomePessoa;
	@Email
	@NotNull
	private String email;
	@NotBlank
	private String senha;
	@NotNull
	private int perfil;
}
