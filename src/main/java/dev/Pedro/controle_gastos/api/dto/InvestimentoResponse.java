package dev.Pedro.controle_gastos.api.dto;

import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import dev.Pedro.controle_gastos.enums.PeriodicidadeTaxa;
import dev.Pedro.controle_gastos.enums.TipoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestimentoResponse(

        Long id,
        String descricao,
        BigDecimal valorAplicado,
        BigDecimal valorAtual,
        LocalDate data,
        CategoriaInvestimento categoria,
        TipoInvestimento tipo,
        boolean isentoIR,
        BigDecimal taxaJuros,
        PeriodicidadeTaxa periodicidadeTaxa,
        LocalDate ultimoSaque,
        BigDecimal rendimento
) {
}
