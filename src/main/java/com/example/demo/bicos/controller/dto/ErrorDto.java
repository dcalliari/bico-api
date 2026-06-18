package com.example.demo.bicos.controller.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDto(
    String message, 
    int status, 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    Instant timestamp,
    String path,
    Object details
) {
    public ErrorDto(String message, int status, String path) {
        this(message, status, Instant.now(), path, null);
    }

    public ErrorDto(String message, int status, String path, Object details) {
        this(message, status, Instant.now(), path, details);
    }
}