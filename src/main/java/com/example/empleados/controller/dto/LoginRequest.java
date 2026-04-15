package com.example.empleados.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String usuario,
        @JsonProperty("contraseña")
        @JsonAlias("contrasena")
        @NotBlank String contrasena) {
}
