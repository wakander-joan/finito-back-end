package com.management.finito.config;

import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PessoaRepository pessoaRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Pessoa pessoa = pessoaRepository.buscaEmail(request.getEmail());
        if (pessoa == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail não cadastrado!");
        }
        if (!passwordEncoder.matches(request.getSenha(), pessoa.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        }
        String token = jwtService.gerarToken(pessoa.getIdPessoa(), pessoa.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
