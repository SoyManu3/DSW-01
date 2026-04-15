package com.example.empleados.domain;

public enum RolEmpleado {
    ADMIN,
    EMPLEADO;

    public String toAuthority() {
        return "ROLE_" + name();
    }
}
