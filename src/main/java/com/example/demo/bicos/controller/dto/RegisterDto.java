package com.example.demo.bicos.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(

    @NotBlank(message= "O campo login necessita ser preenchido") 
    String login,

    @NotBlank(message= "O campo mail necessita ser preenchido") 
    @Email(message="O mail necessita estar no formato correto") 
    String mail, 

    @NotBlank(message= "O campo password necessita ser preenchido")
    @Size(min=4, max=30, message= "A senha deve ter entre 4 e 30 caracteres")
    String password) {
}
