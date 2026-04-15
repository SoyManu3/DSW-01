package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.EmpleadoAuthPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PasswordPolicyTest {

    private EmpleadoAuthPolicyService policyService;

    @BeforeEach
    void setUp() {
        EmpleadoRepository repository = Mockito.mock(EmpleadoRepository.class);
        policyService = new EmpleadoAuthPolicyService(repository);
    }

    @Test
    void validarPassword_debeAceptarPasswordValida() {
        assertDoesNotThrow(() -> policyService.validarPassword("clave123"));
    }

    @Test
    void validarPassword_debeRechazarPasswordSinNumero() {
        assertThrows(IllegalArgumentException.class, () -> policyService.validarPassword("sololetras"));
    }

    @Test
    void validarPassword_debeRechazarPasswordCorta() {
        assertThrows(IllegalArgumentException.class, () -> policyService.validarPassword("a1b2"));
    }
}
