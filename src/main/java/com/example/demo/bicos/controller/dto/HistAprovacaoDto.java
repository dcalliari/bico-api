package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;

import com.example.demo.bicos.models.HistAprovacaoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record HistAprovacaoDto(Long id, HistAprovacaoStatus decisao, String motivo,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataAprovacao) {

}
