package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;

public record ErrorDto(
    String message, 
    int status, 
    LocalDateTime timestamp
) {
    public ErrorDto(String message, int status) {
        this(message, status, LocalDateTime.now());
    }
}