package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.BicosFilter;

public record ListBicosDto(Long id, String name, String description, String cidadeName, BigDecimal price, BicosFilter bicosFilter, Instant dataHoraServico) {
    public ListBicosDto(Bicos bicos){
        this(bicos.getId(), bicos.getName(), bicos.getDescription(), bicos.getCidade().getName(), bicos.getPrice(), bicos.getBicosFilter(), bicos.getDataHoraServico());
    }
}
