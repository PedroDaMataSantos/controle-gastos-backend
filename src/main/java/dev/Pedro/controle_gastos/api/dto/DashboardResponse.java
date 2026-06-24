package dev.Pedro.controle_gastos.api.dto;

import java.math.BigDecimal;

public record DashboardResponse(
        BigDecimal saldoTotal,
        BigDecimal saldoMensal,
        BigDecimal gastosTotal,
        BigDecimal gastosMensal,
        BigDecimal receitaTotal,
        BigDecimal receitaMensal,
        BigDecimal investimentoTotal,
        BigDecimal investimentoMensal,
        BigDecimal patrimonio

) {
}
