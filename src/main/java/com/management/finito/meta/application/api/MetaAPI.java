package com.management.finito.meta.application.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meta")
public interface MetaAPI {
    @PostMapping(value = "/createMeta")
    @ResponseStatus(code = HttpStatus.CREATED)
    MetaResponse createMeta(@Valid @RequestBody MetaRequest metaRequest);

    @GetMapping("/getMetaId/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    MetaDatailResponse getMetaId(@PathVariable int id);

    @GetMapping("/getAllMeta/{idUsuario}")
    @ResponseStatus(code = HttpStatus.OK)
    List<MetaListResponse> getAllMetaUser(@PathVariable UUID idUsuario);

    @DeleteMapping("/deleteMetaId/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    void deleteMetaId(@PathVariable int id);
}
