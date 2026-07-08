package dev.Pedro.controle_gastos.enums;

public enum PeriodicidadeTaxa {

    MENSAL ( "Mensal"),
    ANUAL( "Anual");


    private final String descricao;

    PeriodicidadeTaxa(String descricao) {
        this.descricao = descricao;
    }
}
