package com.example.demo.bicos.infra.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.bicos.controller.dto.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, 
                       AccessDeniedException accessDeniedException) throws IOException {
        
        ErrorDto error = new ErrorDto(
            "Você não tem permissão para acessar este recurso.", 
            HttpServletResponse.SC_FORBIDDEN, 
            request.getRequestURI()
        );
        
        ErrorResponseUtils.writeErrorResponse(response, error);
    }
}