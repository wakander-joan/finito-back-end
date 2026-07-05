package com.management.finito.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/** Telemetria para o painel: acessos (logins) por dia e log de erros da API. */
@Service
@RequiredArgsConstructor
@Log4j2
public class AdminTelemetryService {
    private final AcessoDiarioJPARepository acessoRepo;
    private final ErroLogJPARepository erroRepo;

    @Transactional
    public void registraAcesso() {
        try {
            LocalDate hoje = LocalDate.now();
            AcessoDiario a = acessoRepo.findById(hoje).orElseGet(() -> new AcessoDiario(hoje));
            a.incrementa();
            acessoRepo.save(a);
        } catch (Exception e) {
            log.warn("Falha ao registrar acesso: {}", e.getMessage());
        }
    }

    @Transactional
    public void registraErro(String metodo, String path, int status, String mensagem, String usuarioEmail) {
        try {
            ErroLog e = new ErroLog();
            e.setQuando(Instant.now());
            e.setMetodo(metodo);
            e.setPath(path);
            e.setStatus(status);
            e.setUsuarioEmail(usuarioEmail);
            e.setAcao(AcaoDescriber.descreve(metodo, path));
            e.setMensagem(mensagem != null && mensagem.length() > 800 ? mensagem.substring(0, 800) : mensagem);
            erroRepo.save(e);
        } catch (Exception ex) {
            log.warn("Falha ao registrar erro: {}", ex.getMessage());
        }
    }

    public long acessosHoje() {
        return acessoRepo.findById(LocalDate.now()).map(AcessoDiario::getTotal).orElse(0L);
    }

    public List<AcessoDiario> ultimosAcessos(int dias) {
        return acessoRepo.findByDiaGreaterThanEqualOrderByDiaAsc(LocalDate.now().minusDays(Math.max(1, dias) - 1L));
    }

    public List<ErroLog> ultimosErros() {
        return erroRepo.findTop50ByOrderByQuandoDesc();
    }
}
