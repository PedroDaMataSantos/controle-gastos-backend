package dev.Pedro.controle_gastos.enums;

public enum TipoRegistro {
    SAIDA("Saída"),
    ENTRADA("Entrada");

    private final String descricao;

    TipoRegistro(String descricao) {

        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
