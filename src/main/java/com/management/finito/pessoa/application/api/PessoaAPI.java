package com.management.finito.pessoa.application.api;

import java.util.List;
import java.util.UUID;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pessoa")
public interface PessoaAPI {
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	PessoaResponse criaPessoa(@Valid @RequestBody PessoaRequest pessoaRequeste);

	@GetMapping(value = "/{idPessoa}")
	@ResponseStatus(code = HttpStatus.OK)
	PessoaDetalhadaResponse buscaPessoaPorId(@PathVariable UUID idPessoa);

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	List<PessoaListResponse> buscarTodasPessoas();

	@DeleteMapping(value = "/{idPessoa}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	void deletaPessoaPorId(@PathVariable UUID idPessoa);

	@PatchMapping(value = "/{idPessoa}")
	@ResponseStatus(code = HttpStatus.CREATED)
	void editaPessoaPorId(@PathVariable UUID idPessoa,
						@Valid @RequestBody PessoaAlteracaoRequest pessoaAlteracaoRequest);
}
