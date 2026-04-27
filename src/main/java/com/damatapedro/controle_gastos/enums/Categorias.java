package com.damatapedro.controle_gastos.enums;

public enum Categorias {

    //Gastos
    ALIMENTACAO("Alimentação"),
    LAZER("Lazer"),
    COMBUSTIVEL("Combustível"),
    SAUDE ("Saúde"),
    EDUCACAO ("Educação"),
    FATURA_CARTAO("Fatura do cartão"),
    ALUGUEL("Aluguel"),
    CONTAS("Contas"),
    OUTROS("Outras despesas"),
    TRIBUTO("Impostos"),

    //Receita
    SALARIO("Salario"),
    INVESTIMENTO("Investimento"),
    VALES("Vales"),
    BENEFICIO("Benefícios");

    private final String descricao;

    private Categorias(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

