package com.damatapedro.controle_gastos.enums;

public enum Categorias {

    // GASTOS
    ALIMENTACAO("Alimentação", TipoRegistro.GASTO),
    LAZER("Lazer", TipoRegistro.GASTO),
    COMBUSTIVEL("Combustível", TipoRegistro.GASTO),
    SAUDE("Saúde", TipoRegistro.GASTO),
    EDUCACAO("Educação", TipoRegistro.GASTO),
    FATURA_CARTAO("Fatura do cartão", TipoRegistro.GASTO),
    ALUGUEL("Aluguel", TipoRegistro.GASTO),
    CONTAS("Contas", TipoRegistro.GASTO),
    OUTROS("Outros gastos", TipoRegistro.GASTO),
    TRIBUTO("Impostos", TipoRegistro.GASTO),

    // RECEITAS
    SALARIO("Salário", TipoRegistro.RECEITA),
    INVESTIMENTO("Investimento", TipoRegistro.RECEITA),
    VALES("Vales", TipoRegistro.RECEITA),
    BENEFICIO("Benefícios", TipoRegistro.RECEITA),
    OUTRAS("Outras receitas", TipoRegistro.RECEITA);

    private final String descricao;
    private final TipoRegistro tipo;

    Categorias(String descricao, TipoRegistro tipo) {
        this.descricao = descricao;
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }
}