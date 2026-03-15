package com.management.finito.pessoa.application.api;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
@Builder
public class PessoaResponse {
	private UUID idPessoa;
}
