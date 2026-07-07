package com.example.demo.bicos.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HistAprovacaoStatus {

    APROVADO("A", "Aprovado"),
    REJEITADO("R", "Rejeitado");

    private final String codigo;
    private final String descricao;

    private HistAprovacaoStatus(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static HistAprovacaoStatus doValor(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Código não pode ser null");
        }
        for (HistAprovacaoStatus status : values()) {
            if (status.codigo.equalsIgnoreCase(codigo)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
