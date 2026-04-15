package com.example.empleados.repository;

import com.example.empleados.domain.EventoAutenticacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoAutenticacionRepository extends JpaRepository<EventoAutenticacion, Long> {
}
