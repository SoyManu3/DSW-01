package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

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

    @Column(name = "correo", nullable = false, length = 320, unique = true)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private Instant bloqueadoHasta;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private RolEmpleado rol = RolEmpleado.EMPLEADO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    public Empleado() {
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            String correo,
            String passwordHash,
            boolean activo,
            int intentosFallidos,
            Instant bloqueadoHasta) {
        this(id, nombre, direccion, telefono, correo, passwordHash, activo, intentosFallidos, bloqueadoHasta,
                RolEmpleado.EMPLEADO);
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            String correo,
            String passwordHash,
            boolean activo,
            int intentosFallidos,
            Instant bloqueadoHasta,
            RolEmpleado rol) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.activo = activo;
        this.intentosFallidos = intentosFallidos;
        this.bloqueadoHasta = bloqueadoHasta;
        this.rol = rol;
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            String correo,
            String passwordHash,
            boolean activo,
            int intentosFallidos,
            Instant bloqueadoHasta,
            Departamento departamento) {
        this(id, nombre, direccion, telefono, correo, passwordHash, activo, intentosFallidos, bloqueadoHasta,
                RolEmpleado.EMPLEADO);
        this.departamento = departamento;
    }

    public Empleado(
            EmpleadoId id,
            String nombre,
            String direccion,
            String telefono,
            String correo,
            String passwordHash,
            boolean activo,
            int intentosFallidos,
            Instant bloqueadoHasta,
            RolEmpleado rol,
            Departamento departamento) {
        this(id, nombre, direccion, telefono, correo, passwordHash, activo, intentosFallidos, bloqueadoHasta, rol);
        this.departamento = departamento;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Instant getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    public void setBloqueadoHasta(Instant bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getClave() {
        if (id == null || id.getPrefijo() == null || id.getNumeroAutonumerico() == null) {
            return null;
        }
        return id.getPrefijo() + "-" + id.getNumeroAutonumerico();
    }
}
