package dev.Pedro.controle_gastos.api.dto;

import dev.Pedro.controle_gastos.enums.CategoriaRegistro;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardResponse(
        BigDecimal saldoTotal,
        BigDecimal saldoMensal,
        BigDecimal gastosTotal,
        BigDecimal gastosMensal,
        BigDecimal receitaTotal,
        BigDecimal receitaMensal,
        BigDecimal investimentoTotal,
        BigDecimal investimentoMensal,
        BigDecimal patrimonio,
        Map<CategoriaRegistro, BigDecimal> gastoCategoriaTotal,
        Map<CategoriaRegistro, BigDecimal> gastoCategoriaMensal


) {
}
