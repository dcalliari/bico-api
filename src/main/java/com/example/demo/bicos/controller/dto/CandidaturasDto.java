package com.example.demo.bicos.controller.dto;

import java.time.Instant;

import com.example.demo.bicos.models.CandidaturaStatus;

public record CandidaturasDto(Long id, CandidaturaStatus status, Instant dataSolicitacao) {
    
}
