package com.example.empleados.repository;

import com.example.empleados.domain.Departamento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    boolean existsByNombre(String nombre);

    Optional<Departamento> findByNombre(String nombre);
}
