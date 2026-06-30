package com.example.demo.bicos.controller.dto;

import com.example.demo.bicos.models.User;

public record ListUsersDto(String login, String fullName, String mail, String cpf) {
    public ListUsersDto(User user) {
        this(user.getLogin(), user.getFullName(), user.getMail(), user.getCpf());
    }
}
