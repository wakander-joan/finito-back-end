package com.management.finito.meta.application.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class UpdateMetaRequest {
    @NotBlank
    private String description;
    private Double value;
    private int month;
    private int year;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dataVencimento;
}
