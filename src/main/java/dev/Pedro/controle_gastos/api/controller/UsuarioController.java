package dev.Pedro.controle_gastos.api.controller;

import dev.Pedro.controle_gastos.api.dto.UsuarioRequest;
import dev.Pedro.controle_gastos.api.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuarios")
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
