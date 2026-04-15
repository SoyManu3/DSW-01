package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.EmpleadoAuthPolicyService;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EmpleadoAuthPolicyServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    private EmpleadoAuthPolicyService policyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policyService = new EmpleadoAuthPolicyService(empleadoRepository);
    }

    @Test
    void registrarIntentoFallido_debeBloquearEnQuintoIntento() {
        Empleado empleado = new Empleado(
                new EmpleadoId("EMP", 1L),
                "Nombre",
                "Dir",
                "Tel",
                "user@test.com",
                "hash",
                true,
                4,
                null);

        when(empleadoRepository.findByCorreoIgnoreCase("user@test.com")).thenReturn(Optional.of(empleado));

        policyService.registrarIntentoFallido("user@test.com");

        assertEquals(5, empleado.getIntentosFallidos());
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void desbloquearSiCorresponde_debeResetearContadorSiExpirado() {
        Empleado empleado = new Empleado(
                new EmpleadoId("EMP", 1L),
                "Nombre",
                "Dir",
                "Tel",
                "user@test.com",
                "hash",
                true,
                5,
                Instant.now().minusSeconds(60));

        policyService.desbloquearSiCorresponde(empleado);

        assertEquals(0, empleado.getIntentosFallidos());
        assertEquals(null, empleado.getBloqueadoHasta());
        verify(empleadoRepository).save(empleado);
    }
}
