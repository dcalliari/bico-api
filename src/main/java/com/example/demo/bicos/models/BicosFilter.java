package com.example.demo.bicos.models;

public enum BicosFilter {
    FAXINA("faxina"),
    ENTREGAS("entregas"),
    MANUTENCAO("manutenção"),
    TI("ti"),
    EVENTOS("eventos"),
    OUTROS("outros");

    private final String filter;

    private BicosFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

}
