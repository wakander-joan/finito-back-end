package com.management.finito.admin;

import com.management.finito.handler.APIException;
import com.management.finito.pessoa.application.repository.PessoaRepository;
import com.management.finito.pessoa.domain.Pessoa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Ações destrutivas do admin. Apagar um usuário remove TUDO dele (lançamentos,
 * metas+etapas, assinatura) e a própria conta — de forma ATÔMICA (rollback se falhar).
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AdminService {
    private final PessoaRepository pessoaRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void apagaUsuarioCompleto(String email) {
        Pessoa pessoa = pessoaRepository.buscaEmail(email == null ? null : email.trim());
        if (pessoa == null) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + email);
        }
        UUID id = pessoa.getIdPessoa();
        int lanc = em.createQuery("DELETE FROM Lancamento l WHERE l.idPessoa = :id").setParameter("id", id).executeUpdate();
        em.createQuery("DELETE FROM Etapa e WHERE e.idMeta IN (SELECT m.idMeta FROM Meta m WHERE m.idUsuario = :id)").setParameter("id", id).executeUpdate();
        int metas = em.createQuery("DELETE FROM Meta m WHERE m.idUsuario = :id").setParameter("id", id).executeUpdate();
        em.createQuery("DELETE FROM Assinatura a WHERE a.idUsuario = :id").setParameter("id", id).executeUpdate();
        em.createQuery("DELETE FROM Pessoa p WHERE p.idPessoa = :id").setParameter("id", id).executeUpdate();
        log.warn("[admin] Usuario APAGADO 100%: {} (lancamentos={}, metas={})", email, lanc, metas);
    }
}
