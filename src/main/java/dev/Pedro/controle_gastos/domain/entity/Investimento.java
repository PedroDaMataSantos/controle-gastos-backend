package dev.Pedro.controle_gastos.domain.entity;

import dev.Pedro.controle_gastos.enums.CategoriaInvestimento;
import dev.Pedro.controle_gastos.enums.TipoInvestimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
    @Table(name="investimento")
    @Getter
    @Setter
    @NoArgsConstructor

public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao",nullable = true,length = 200 )
    private String descricao;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Column(name = "valorAplicado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAplicado;

    @NotNull
    @Column(name = "data", nullable = false )
    private LocalDate data;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 20)
    private CategoriaInvestimento categoria;


    public Investimento(String descricao, BigDecimal valorAplicado, LocalDate data, CategoriaInvestimento categoria) {
        this.descricao = descricao;
        this.valorAplicado = valorAplicado;
        this.data = data;
        this.categoria = categoria;
    }
}
