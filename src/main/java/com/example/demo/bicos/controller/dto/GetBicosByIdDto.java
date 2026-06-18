package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.bicos.models.BicosFilter;

public record GetBicosByIdDto(String id, String name, String description, String cidadeName, BigDecimal price, BicosFilter bicosFilter, Instant dataHoraServico, Instant created_at, Instant updated_at, Instant deleted_at) {
    
}
