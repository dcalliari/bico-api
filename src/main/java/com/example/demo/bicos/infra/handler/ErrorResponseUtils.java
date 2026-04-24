package com.example.demo.bicos.infra.handler;

import java.io.IOException;

import com.example.demo.bicos.controller.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseUtils {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static void writeErrorResponse(HttpServletResponse response, ErrorDto errorDto) throws IOException {
        response.setStatus(errorDto.status());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(errorDto));
    }
}
