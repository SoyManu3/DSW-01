package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.empleados.domain.Departamento;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.DepartamentoService;
import com.example.empleados.service.exception.DepartamentoConEmpleadosException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DepartamentoDeletionPolicyTest {

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
    void eliminar_debeRechazarSiTieneEmpleados() {
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(new Departamento(1L, "Ventas")));
        when(empleadoRepository.existsByDepartamentoId(1L)).thenReturn(true);

        assertThrows(DepartamentoConEmpleadosException.class, () -> departamentoService.eliminar(1L));
    }
}
