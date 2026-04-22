package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.bicos.models.Bicos;
import com.example.demo.bicos.models.BicosFilter;

public record ListBicosDto(Long id, String name, String description, String city, BigDecimal price, BicosFilter bicosFilter, LocalDateTime dataHoraServico) {
    public ListBicosDto(Bicos bicos){
        this(bicos.getId(), bicos.getName(), bicos.getDescription(), bicos.getCity(), bicos.getPrice(), bicos.getBicosFilter(), bicos.getDataHoraServico());
    }
}
