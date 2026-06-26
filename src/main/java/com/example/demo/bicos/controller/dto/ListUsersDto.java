package com.example.demo.bicos.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.bicos.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ListUsersDto(UUID id, String login, String fullName, String mail, String cpf, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime createdAt, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime updatedAt, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime deletedAt) {
    public ListUsersDto(User user){
        this(user.getId(), user.getLogin(), user.getFullName(), user.getMail(), user.getCpf(), user.getCreatedAt(), user.getUpdatedAt(), user.getDeletedAt());
    }
}
