package com.example.demo.bicos.controller.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.demo.bicos.models.User;

public record ListUsersDto(UUID id, String login, String mail, Instant createdAt, Instant deletedAt) {
    public ListUsersDto(User user){
        this(user.getId(), user.getLogin(), user.getMail(), user.getCreatedAt(), user.getDeletedAt());
    }
}
