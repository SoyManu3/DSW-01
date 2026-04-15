package com.example.empleados.service;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EventoAutenticacion;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.repository.EventoAutenticacionRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationAuditService {

    private final EventoAutenticacionRepository eventoAutenticacionRepository;
    private final EmpleadoRepository empleadoRepository;

    public AuthenticationAuditService(
            EventoAutenticacionRepository eventoAutenticacionRepository,
            EmpleadoRepository empleadoRepository) {
        this.eventoAutenticacionRepository = eventoAutenticacionRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public void registrar(String correo, String resultado, String origen) {
        String email = correo == null ? "" : correo.trim().toLowerCase();
        String empleadoClave = empleadoRepository.findByCorreoIgnoreCase(email)
                .map(Empleado::getClave)
                .orElse(null);

        EventoAutenticacion evento = new EventoAutenticacion(
                email,
                empleadoClave,
                resultado,
                Instant.now(),
                origen);
        eventoAutenticacionRepository.save(evento);
    }
}
