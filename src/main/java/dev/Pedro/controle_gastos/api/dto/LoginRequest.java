package dev.Pedro.controle_gastos.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        String email,
        String senha
) {
}