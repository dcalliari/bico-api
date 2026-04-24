package com.example.demo.bicos.infra.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.bicos.controller.dto.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        
        ErrorDto error = new ErrorDto(
            "Acesso negado. Token JWT ausente, expirado ou inválido.", 
            HttpServletResponse.SC_UNAUTHORIZED, 
            request.getRequestURI()
        );
        
        ErrorResponseUtils.writeErrorResponse(response, error);
    }
}