package com.example.demo.bicos.controller;

import com.example.demo.bicos.controller.dto.NotificationDTO;
import com.example.demo.bicos.models.User;
import com.example.demo.bicos.repo.NotificationRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Notificações")
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Operation(summary = "Notificações do usuário autenticado")
    @GetMapping("/me")
    public List<NotificationDTO> myNotifications() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();

        return notificationRepository.findByUser_IdOrderByCreatedAtDesc(user.getId());
    }
}
