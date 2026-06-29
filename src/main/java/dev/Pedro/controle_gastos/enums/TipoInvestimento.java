package dev.Pedro.controle_gastos.enums;

public enum TipoInvestimento {
    APORTE("Aporte Investimento"),
    INVESTIMENTO("Investimento externo");

    private final String descricao;

    TipoInvestimento(String descricao) {

        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
