package dev.Pedro.controle_gastos.enums;

public enum TipoRegistro {
    SAIDA("Gasto"),
    ENTRADA("Receita");

    private final String descricao;

    TipoRegistro(String descricao) {

        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
