package com.example.demo.bicos.controller.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserDto(String login, String fullName, @Email String mail) {
    
}
