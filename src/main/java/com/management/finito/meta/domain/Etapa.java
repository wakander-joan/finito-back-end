package com.management.finito.meta.domain;

import com.management.finito.meta.application.api.EtapaRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Log4j2
public class Etapa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID idEtapa;
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private int numero;
    private UUID idMeta;
    private String descricao;
    private String anotacao;
    private StatusEtapa status = StatusEtapa.PENDENTE;
    private Date dataInicioAndamento;
    private Date dataInicioImpedimento;
    private Date dataConclusao;

    public Etapa(EtapaRequest c, UUID idMeta) {
        this.idMeta = idMeta;
        this.numero = c.getNumero();
        this.descricao = c.getDescricao();
        this.anotacao = c.getAnotacao();
    }
}














