package com.management.finito.pessoa.application.api;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PessoaAlteracaoRequest {
	private String nomePessoa;
	@Email
	private String email;
}
