package com.management.finito.meta.domain;

import com.management.finito.meta.application.api.MetaRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
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
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID idMeta;
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID idUsuario;
    private String descricao;
    private String anotacao;
    private int totalEtapas = 0;
    private int totalEtapasConcluidas = 0;
    private LocalDate dataInicial;
    private LocalDate dataAlvo;

    public Meta(MetaRequest metaRequest, UUID idPessoa) {
        this.idUsuario = idPessoa;
        this.descricao = metaRequest.getDescricao();
        this.anotacao = metaRequest.getAnotacao();
        this.dataInicial = metaRequest.getDataInicial();
        this.dataAlvo = metaRequest.getDataAlvo();
    }
}
