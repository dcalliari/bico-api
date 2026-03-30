package com.example.demo.bicos.controller.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.demo.bicos.models.UserRole;

public record GetUserByIdDto(UUID id, String login, String mail, UserRole role, Instant createdAt, Instant updatedAt, Instant deletedAt) {
    
}
