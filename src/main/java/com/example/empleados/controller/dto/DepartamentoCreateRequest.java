package com.example.empleados.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DepartamentoCreateRequest {

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^(?=.*\\S).{1,100}$")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
