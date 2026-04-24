package com.example.demo.bicos.infra.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bicos.controller.dto.ErrorDto;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        ErrorDto errorDto = new ErrorDto("Erro de validação nos campos", 400, request.getRequestURI(), errors);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDto> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
        ErrorDto error = new ErrorDto("Recurso não encontrado", 404, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        String msg = ex.getReason() != null ? ex.getReason() : "Erro na operação";
        ErrorDto error = new ErrorDto(msg, ex.getStatusCode().value(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorDto error = new ErrorDto("Ocorreu um erro interno no servidor", 500, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleLogin(Exception ex, HttpServletRequest request) {
        ErrorDto error = new ErrorDto("Erro no login ou senha", 401, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
    ErrorDto error = new ErrorDto("Você não tem permissão para acessar este recurso.",403, request.getRequestURI());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}