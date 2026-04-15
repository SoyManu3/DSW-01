package com.example.empleados.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationStatusEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AuthenticationStatusEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        try {
            HttpStatus status = authException instanceof LockedException
                    ? HttpStatus.LOCKED
                    : HttpStatus.UNAUTHORIZED;

            ApiError error = new ApiError(
                    Instant.now(),
                    status.value(),
                    status.getReasonPhrase(),
                    authException.getMessage(),
                    request.getRequestURI(),
                    Map.of());

            response.setStatus(status.value());
            response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), error);
        } catch (Exception ignored) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
