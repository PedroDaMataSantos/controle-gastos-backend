package dev.Pedro.controle_gastos.api.dto;

import java.math.BigDecimal;

public record PrevisaoSaqueResponse(
        BigDecimal valorBruto,
        BigDecimal iof,
        BigDecimal ir,
        BigDecimal valorDisponivel
) {}
