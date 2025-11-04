package com.management.finito.lancamento.infra;

import com.management.finito.lancamento.domain.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LancamentoJPARepository  extends JpaRepository<Lancamento, UUID> {
    void deleteAllByIdPessoa(UUID idPessoa);
    List<Lancamento> findAllByMesDoLancamentoAndIdPessoaAndAno(int id, UUID idPessoa, int ano);
    List<Lancamento> findAllByDataVencimentoAndTipo(LocalDate hoje, int i);
    void deleteAllByIdMeta(int id);
    List<Lancamento> findAllByIdMeta(int id);
    List<Lancamento> findAllByIdRecorrencia(int idRecorrencia);
    List<Lancamento> findByIdLancamentoIn(List<UUID> idLancamento);
    List<Lancamento> findAllByIdParcela(int idParcela);
}
