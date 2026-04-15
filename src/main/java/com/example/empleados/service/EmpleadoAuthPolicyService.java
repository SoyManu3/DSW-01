package com.example.empleados.service;

import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.EmpleadoRepository;
import java.time.Instant;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoAuthPolicyService {

    private static final Pattern PASSWORD_POLICY = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoAuthPolicyService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void validarPassword(String password) {
        if (password == null || !PASSWORD_POLICY.matcher(password).matches()) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 8 caracteres, al menos 1 letra y 1 número");
        }
    }

    @Transactional
    public void registrarIntentoFallido(String correo) {
        empleadoRepository.findByCorreoIgnoreCase(correo).ifPresent(empleado -> {
            if (empleado.getBloqueadoHasta() != null && empleado.getBloqueadoHasta().isAfter(Instant.now())) {
                return;
            }
            int intentos = empleado.getIntentosFallidos() + 1;
            empleado.setIntentosFallidos(intentos);
            if (intentos >= MAX_FAILED_ATTEMPTS) {
                empleado.setBloqueadoHasta(Instant.now().plusSeconds(15 * 60));
            }
            empleadoRepository.save(empleado);
        });
    }

    @Transactional
    public void registrarAutenticacionExitosa(String correo) {
        empleadoRepository.findByCorreoIgnoreCase(correo).ifPresent(empleado -> {
            empleado.setIntentosFallidos(0);
            empleado.setBloqueadoHasta(null);
            empleadoRepository.save(empleado);
        });
    }

    @Transactional
    public void desbloquearSiCorresponde(Empleado empleado) {
        if (empleado.getBloqueadoHasta() != null && !empleado.getBloqueadoHasta().isAfter(Instant.now())) {
            empleado.setBloqueadoHasta(null);
            empleado.setIntentosFallidos(0);
            empleadoRepository.save(empleado);
        }
    }

    public boolean estaBloqueado(Empleado empleado) {
        return empleado.getBloqueadoHasta() != null && empleado.getBloqueadoHasta().isAfter(Instant.now());
    }
}
