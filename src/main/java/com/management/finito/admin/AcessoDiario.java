package com.management.finito.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Contador de acessos (logins) por dia. */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AcessoDiario {
    @Id
    private LocalDate dia;
    private long total;

    public AcessoDiario(LocalDate dia) {
        this.dia = dia;
        this.total = 0;
    }

    public void incrementa() {
        this.total++;
    }
}
