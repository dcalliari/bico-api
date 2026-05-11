package com.example.demo.bicos.controller.dto;

import java.math.BigDecimal;

import com.example.demo.bicos.models.CandidaturaStatus;

public record CandidaturaPendenteDto(Long id, CandidaturaStatus status,String user, String bico, String bicoTipo, BigDecimal bicoValor) {
    
}
