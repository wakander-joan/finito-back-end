package com.management.finito.pessoa.infra;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.finito.pessoa.domain.Pessoa;

public interface PessoaSpringDataJPARepository extends JpaRepository<Pessoa, UUID>{
    Pessoa findByEmail(String email);
}