package com.management.finito.lancamento.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class IdRecorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
