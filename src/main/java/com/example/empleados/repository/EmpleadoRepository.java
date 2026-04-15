package com.example.empleados.repository;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {

    Optional<Empleado> findTopByIdPrefijoOrderByIdNumeroAutonumericoDesc(String prefijo);

    Optional<Empleado> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    Page<Empleado> findByDepartamentoIdOrderByIdNumeroAutonumericoAsc(Long departamentoId, Pageable pageable);

    long countByDepartamentoId(Long departamentoId);

    boolean existsByDepartamentoId(Long departamentoId);
}
