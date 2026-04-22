package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.bicos.models.BicosFilter;

public record UpdateBicosDto(String name, String description, String city, BigDecimal price, BicosFilter bicosFilter, LocalDateTime dataHoraServico) {
    
}
