package com.example.demo.bicos.controller.dto;

import java.io.Serializable;

public record NotificationDTO(Long id, String title, String description) implements Serializable {
}