package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.example.demo.bicos.models.BicosFilter;

public record GetBicosByIdDto(String id, String name, String description, String city, BigDecimal price, BicosFilter bicosFilter, LocalDateTime dataHoraServico, Instant created_at, Instant updated_at, Instant deleted_at) {
    
}
