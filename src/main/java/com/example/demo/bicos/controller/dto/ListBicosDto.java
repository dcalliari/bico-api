package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.BicosFilter;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ListBicosDto(Long id, String name, String description, String cidadeName, BigDecimal price, BicosFilter bicosFilter, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataHoraServico) {
    public ListBicosDto(Bicos bicos){
        this(bicos.getId(), bicos.getName(), bicos.getDescription(), bicos.getCidade().getName(), bicos.getPrice(), bicos.getBicosFilter(), bicos.getDataHoraServico());
    }
}
