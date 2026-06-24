package dev.Pedro.controle_gastos.api.dto;

import dev.Pedro.controle_gastos.enums.CategoriaRegistro;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


public record RegistroRequest(

        TipoRegistro tipoRegistro,
        CategoriaRegistro categoria,
        String descricao,
        BigDecimal valor,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate data

) {
}
