package com.example.empleados.controller.dto;

public class EmpleadoDepartamentoResponse {

    private String clave;
    private String nombre;
    private String correo;
    private Long departamentoId;

    public EmpleadoDepartamentoResponse() {
    }

    public EmpleadoDepartamentoResponse(String clave, String nombre, String correo, Long departamentoId) {
        this.clave = clave;
        this.nombre = nombre;
        this.correo = correo;
        this.departamentoId = departamentoId;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }
}
