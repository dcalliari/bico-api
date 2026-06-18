package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.bicos.models.BicosFilter;

public record UpdateBicosDto(String name, String description, Long cidadeId, BigDecimal price, BicosFilter bicosFilter, Instant dataHoraServico) {
    
}
