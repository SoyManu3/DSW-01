package com.example.empleados.repository;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {

    Optional<Empleado> findTopByIdPrefijoOrderByIdNumeroAutonumericoDesc(String prefijo);
}
