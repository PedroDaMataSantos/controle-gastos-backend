package dev.Pedro.controle_gastos.api.controller;

import dev.Pedro.controle_gastos.api.dto.UsuarioRequest;
import dev.Pedro.controle_gastos.api.dto.UsuarioResponse;
import dev.Pedro.controle_gastos.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse create(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        return service.create(usuarioRequest);
    }
}
