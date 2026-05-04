package dev.Pedro.controle_gastos.domain.model;

import dev.Pedro.controle_gastos.enums.Categorias;
import dev.Pedro.controle_gastos.enums.TipoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
    @Table(name="registro")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public class Registro{
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private int id;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(name = "tipo_registro", nullable = false, length = 15)
        private TipoRegistro tipoRegistro;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(name = "categoria", nullable = false, length = 15)
        private Categorias categoria;


        @Size(max = 200)
        @Column(name = "descrição",nullable = true,length = 200 )
        private String descricao;

        @NotNull
        @Digits(integer = 10, fraction = 2)
        @Column(name = "valor", nullable = false, precision = 12, scale = 2)
         private BigDecimal valor;

        @NotNull
        @Column(name = "data", nullable = false)
        private LocalDate data;
        }





