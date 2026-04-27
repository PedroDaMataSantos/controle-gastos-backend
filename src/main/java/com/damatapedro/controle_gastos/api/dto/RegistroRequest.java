package com.damatapedro.controle_gastos.api.dto;

import com.damatapedro.controle_gastos.enums.Categorias;
import com.damatapedro.controle_gastos.enums.TipoRegistro;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroRequest(

        @NotNull(message = "Tipo é obrigatório")
        TipoRegistro tipoRegistro,

        @NotNull(message = "Categoria é obrigatória")
        Categorias categoria,

        @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @Digits(integer = 10, fraction = 2, message = "Valor inválido")
        BigDecimal valor,

        @NotNull(message = "Data é obrigatória")
        LocalDate data

        ) {
}
