package com.management.finito.lancamento.infra;

import com.management.finito.lancamento.domain.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LancamentoJPARepository  extends JpaRepository<Lancamento, UUID> {
    void deleteAllByIdPessoa(UUID idPessoa);
    List<Lancamento> findAllByMesDoLancamentoAndIdPessoa(int id, UUID idPessoa);
}
