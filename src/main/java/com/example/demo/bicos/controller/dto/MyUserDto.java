package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.bicos.models.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;

public record MyUserDto(UUID id, String login, String fullName, String mail, String cpf, UserRole role ,@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime updatedAt, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime deletedAt) {
    
}
