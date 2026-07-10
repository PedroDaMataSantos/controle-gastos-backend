package dev.Pedro.controle_gastos.enums;

public enum CategoriaInvestimento {

    // RECEITAS
    CDB("CDB"),
    LCI("LCI"),
    LCA("LCA"),
    POUPANCA("Poupança" ),
    OUTROS("Outros Investimentos");


    private final String descricao;


    CategoriaInvestimento(String descricao) {
        this.descricao = descricao;

    }

    public String getDescricao() {
        return descricao;
    }

}

