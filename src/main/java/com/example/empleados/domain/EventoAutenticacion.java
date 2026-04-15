package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "evento_autenticacion")
public class EventoAutenticacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correo", nullable = false, length = 320)
    private String correo;

    @Column(name = "empleado_clave", length = 50)
    private String empleadoClave;

    @Column(name = "resultado", nullable = false, length = 40)
    private String resultado;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "origen", length = 255)
    private String origen;

    public EventoAutenticacion() {
    }

    public EventoAutenticacion(String correo, String empleadoClave, String resultado, Instant createdAt, String origen) {
        this.correo = correo;
        this.empleadoClave = empleadoClave;
        this.resultado = resultado;
        this.createdAt = createdAt;
        this.origen = origen;
    }

    public Long getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEmpleadoClave() {
        return empleadoClave;
    }

    public void setEmpleadoClave(String empleadoClave) {
        this.empleadoClave = empleadoClave;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
