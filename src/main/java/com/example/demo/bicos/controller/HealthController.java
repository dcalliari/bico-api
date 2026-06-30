package com.example.demo.bicos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Endpoints de verificação de saúde da aplicação")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check da aplicação", description = "Retorna o status da aplicação")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("UP");
    }
}
