package com.example.demo.bicos.repo;

import com.example.demo.bicos.controller.dto.NotificationDTO;
import com.example.demo.bicos.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<NotificationDTO> findByUser_IdOrderByCreatedAtDesc(UUID userId);
}
