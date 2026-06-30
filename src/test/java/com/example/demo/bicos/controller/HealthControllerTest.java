package com.example.demo.bicos.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// IMPORTANTE: Adicione este import estático
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Test
    @DisplayName("Deve retornar status saudável no endpoint de health check")
    void healthCheckShouldReturnOk() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UP")));
    }

    @Test
    @DisplayName("Deve responder com headers CORS para requisições do frontend")
    void healthCheckShouldExposeCorsHeaders() throws Exception {
        String originParaTeste = allowedOrigins.split(",")[0].trim();

        mockMvc.perform(options("/health")
                .header("Origin", originParaTeste)
                .header("Access-Control-Request-Method", "GET")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", originParaTeste));
    }
}