package com.example.empleados.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmpleadoId implements Serializable {

    @Column(name = "prefijo", nullable = false, length = 3)
    private String prefijo;

    @Column(name = "numero_autonumerico", nullable = false)
    private Long numeroAutonumerico;

    public EmpleadoId() {
    }

    public EmpleadoId(String prefijo, Long numeroAutonumerico) {
        this.prefijo = prefijo;
        this.numeroAutonumerico = numeroAutonumerico;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Long getNumeroAutonumerico() {
        return numeroAutonumerico;
    }

    public void setNumeroAutonumerico(Long numeroAutonumerico) {
        this.numeroAutonumerico = numeroAutonumerico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmpleadoId empleadoId = (EmpleadoId) o;
        return Objects.equals(prefijo, empleadoId.prefijo)
                && Objects.equals(numeroAutonumerico, empleadoId.numeroAutonumerico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefijo, numeroAutonumerico);
    }
}
