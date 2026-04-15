package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.empleados.controller.dto.DepartamentoCreateRequest;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.DepartamentoService;
import com.example.empleados.service.exception.DepartamentoDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DepartamentoUniquenessPolicyTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    private DepartamentoService departamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        departamentoService = new DepartamentoService(departamentoRepository, empleadoRepository);
    }

    @Test
    void crear_debeRechazarNombreDuplicadoExacto() {
        DepartamentoCreateRequest request = new DepartamentoCreateRequest();
        request.setNombre("Ventas");

        when(departamentoRepository.existsByNombre("Ventas")).thenReturn(true);

        assertThrows(DepartamentoDuplicadoException.class, () -> departamentoService.crear(request));
    }
}
