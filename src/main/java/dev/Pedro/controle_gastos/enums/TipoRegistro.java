package dev.Pedro.controle_gastos.enums;

public enum TipoRegistro {
    GASTO("Gasto"),
    RECEITA("Receita"),
    INVESTIMENTO("Investimento");;

    private final String descricao;

    TipoRegistro(String descricao) {

        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
