package com.example.empleados.service.exception;

public class EmpleadoConflictException extends IllegalStateException {

    public EmpleadoConflictException(String message) {
        super(message);
    }
}
