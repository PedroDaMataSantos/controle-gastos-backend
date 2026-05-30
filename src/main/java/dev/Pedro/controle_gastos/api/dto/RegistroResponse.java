package dev.Pedro.controle_gastos.api.dto;

import dev.Pedro.controle_gastos.enums.Categorias;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import java.math.BigDecimal;
import java.time.LocalDate;



public record RegistroResponse(
        Long id,
        TipoRegistro tipoRegistro,
        Categorias categoria,
        String descricao,
        BigDecimal valor,
        LocalDate data

) {
}
