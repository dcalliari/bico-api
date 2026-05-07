package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDto(
    String message, 
    int status, 
    LocalDateTime timestamp,
    String path,
    Object details
) {
    public ErrorDto(String message, int status, String path) {
        this(message, status, LocalDateTime.now(), path, null);
    }

    public ErrorDto(String message, int status, String path, Object details) {
        this(message, status, LocalDateTime.now(), path, details);
    }
}