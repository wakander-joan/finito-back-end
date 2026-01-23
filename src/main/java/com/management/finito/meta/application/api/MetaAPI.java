package com.management.finito.meta.application.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/meta")
public interface MetaAPI {

    @PostMapping("/cadastraMeta")
    @ResponseStatus(code = HttpStatus.CREATED)
    MetaResponse cadastraMeta (@Valid @RequestBody MetaRequest metatoRequest);

    @GetMapping("/buscaMetas")
    @ResponseStatus(code = HttpStatus.OK)
    List<MetaDetalhadaResponse> buscaMetas();

}
