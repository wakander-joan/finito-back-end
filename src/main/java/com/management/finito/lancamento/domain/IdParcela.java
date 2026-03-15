package com.management.finito.lancamento.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Log4j2
public class IdParcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parcela")
    private int id;
}
