package com.example.empleados.controller.dto;

public class DepartamentoResponse {

    private Long id;
    private String nombre;
    private long empleadosCount;

    public DepartamentoResponse() {
    }

    public DepartamentoResponse(Long id, String nombre, long empleadosCount) {
        this.id = id;
        this.nombre = nombre;
        this.empleadosCount = empleadosCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getEmpleadosCount() {
        return empleadosCount;
    }

    public void setEmpleadosCount(long empleadosCount) {
        this.empleadosCount = empleadosCount;
    }
}
