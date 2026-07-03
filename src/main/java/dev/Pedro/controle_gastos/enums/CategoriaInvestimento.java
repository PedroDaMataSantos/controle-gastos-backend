package dev.Pedro.controle_gastos.enums;

public enum CategoriaInvestimento {

    // RECEITAS
    RENDA_FIXA("Renda Fixa"),
    RENDA_VARIAVEL("Renda Variável"),
    BITCOIN("BITCOIN"),
    POUPANCA("Poupança" ),
    FUNDO_IMOBILIARIO("Fundo" ),
    OUTROS("Outros Investimentos");


    private final String descricao;


    CategoriaInvestimento(String descricao) {
        this.descricao = descricao;

    }

    public String getDescricao() {
        return descricao;
    }

}

