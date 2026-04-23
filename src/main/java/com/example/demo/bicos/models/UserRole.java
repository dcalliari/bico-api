package com.example.demo.bicos.models;

public enum UserRole {
    ADMIN("admin"),
    APROVADOR_N1("aprovador_n1"),
    APROVADOR_N2("aprovador_n2"),
    APROVADOR_N3("aprovador_n3"),
    FREELANCER("freelancer");

    private final String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
