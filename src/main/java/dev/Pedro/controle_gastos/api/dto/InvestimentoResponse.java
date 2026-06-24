package dev.Pedro.controle_gastos.api.dto;

import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoRegistro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestimentoResponse(

        Long id,
        String descricao,
        BigDecimal valorAplicado,
        LocalDate data,
        CategoriaInvestimento categoria
) {
}
