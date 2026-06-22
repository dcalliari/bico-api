package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;

import com.example.demo.bicos.models.CandidaturaStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CandidaturasDto(Long id, CandidaturaStatus status, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataSolicitacao, String bicos) {
    
}
