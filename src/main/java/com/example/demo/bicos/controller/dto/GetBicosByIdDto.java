package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.bicos.models.BicosFilter;
import com.fasterxml.jackson.annotation.JsonFormat;

public record GetBicosByIdDto(String id, String name, String description, String cidadeName, BigDecimal price, BicosFilter bicosFilter, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataHoraServico, LocalDateTime created_at, LocalDateTime updated_at, LocalDateTime deleted_at) {
    
}
