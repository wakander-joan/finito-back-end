package com.management.finito.config;

import com.management.finito.handler.APIException;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PessoaRepository pessoaRepository;

    public JwtFilter(JwtService jwtService, PessoaRepository pessoaRepository) {
        this.jwtService = jwtService;
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtService.validarToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extrairEmail(token);
        Pessoa pessoa = pessoaRepository.buscaEmail(email);
        if (pessoa == null){
            throw APIException.build(HttpStatus.NOT_FOUND, "Email não cadastrado!");
        }

        if (pessoa != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    pessoa, null, new ArrayList<>());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
