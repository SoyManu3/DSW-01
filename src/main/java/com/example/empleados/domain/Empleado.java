package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "empleados")
public class Empleado {

    @EmbeddedId
    private EmpleadoId id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 100)
    private String telefono;

    public Empleado() {
    }

    public Empleado(EmpleadoId id, String nombre, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public EmpleadoId getId() {
        return id;
    }

    public void setId(EmpleadoId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getClave() {
        if (id == null || id.getPrefijo() == null || id.getNumeroAutonumerico() == null) {
            return null;
        }
        return id.getPrefijo() + "-" + id.getNumeroAutonumerico();
    }
}
