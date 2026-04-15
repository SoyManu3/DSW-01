package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.empleados.controller.dto.DepartamentoCreateRequest;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.DepartamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DepartamentoValidationTest {

    private DepartamentoService departamentoService;

    @BeforeEach
    void setUp() {
        DepartamentoRepository departamentoRepository = Mockito.mock(DepartamentoRepository.class);
        EmpleadoRepository empleadoRepository = Mockito.mock(EmpleadoRepository.class);
        departamentoService = new DepartamentoService(departamentoRepository, empleadoRepository);
    }

    @Test
    void crear_debeRechazarNombreVacio() {
        DepartamentoCreateRequest request = new DepartamentoCreateRequest();
        request.setNombre("   ");

        assertThrows(IllegalArgumentException.class, () -> departamentoService.crear(request));
    }

    @Test
    void crear_debeRechazarNombreMayorA100() {
        DepartamentoCreateRequest request = new DepartamentoCreateRequest();
        request.setNombre("a".repeat(101));

        assertThrows(IllegalArgumentException.class, () -> departamentoService.crear(request));
    }
}
