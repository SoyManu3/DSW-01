package com.example.empleados.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.empleados.controller.dto.EmpleadoCreateRequest;
import com.example.empleados.controller.dto.EmpleadoResponse;
import com.example.empleados.controller.dto.EmpleadoUpdateRequest;
import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.EmpleadoAuthPolicyService;
import com.example.empleados.service.EmpleadoService;
import com.example.empleados.service.mapper.EmpleadoMapper;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoMapper empleadoMapper;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmpleadoAuthPolicyService authPolicyService;

    @InjectMocks
    private EmpleadoService empleadoService;

    private EmpleadoCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new EmpleadoCreateRequest();
        createRequest.setNombre("Juan Pérez");
        createRequest.setDireccion("Calle 123");
        createRequest.setTelefono("5551234");
        createRequest.setCorreo("juan@example.com");
        createRequest.setPassword("clave123");
        createRequest.setDepartamentoId(1L);
    }

    @Test
    void crear_debeGenerarClaveEmp1_siNoHayRegistrosPrevios() {
        Empleado empleadoGuardado = new Empleado(
            new EmpleadoId("EMP", 1L),
            "Juan Pérez",
            "Calle 123",
            "5551234",
            "juan@example.com",
            "hash",
            true,
            0,
            (Instant) null);
        EmpleadoResponse response = new EmpleadoResponse("EMP-1", "Juan Pérez", "Calle 123", "5551234", "juan@example.com", true);

        when(empleadoRepository.findTopByIdPrefijoOrderByIdNumeroAutonumericoDesc("EMP")).thenReturn(Optional.empty());
        when(empleadoRepository.existsByCorreoIgnoreCase("juan@example.com")).thenReturn(false);
        when(empleadoRepository.existsById(new EmpleadoId("EMP", 1L))).thenReturn(false);
        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(new Departamento(1L, "General")));
        when(passwordEncoder.encode("clave123")).thenReturn("hash");
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleadoGuardado);
        when(empleadoMapper.toResponse(empleadoGuardado)).thenReturn(response);

        EmpleadoResponse result = empleadoService.crear(createRequest);

        assertEquals("EMP-1", result.getClave());
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void actualizar_debeFallar_siClaveNoExiste() {
        EmpleadoUpdateRequest updateRequest = new EmpleadoUpdateRequest();
        updateRequest.setNombre("Nuevo Nombre");
        updateRequest.setDireccion("Nueva Dirección");
        updateRequest.setTelefono("5558888");

        when(empleadoRepository.findById(new EmpleadoId("EMP", 99L))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> empleadoService.actualizar("EMP-99", updateRequest));
    }

    @Test
    void obtenerPorClave_debeFallar_siFormatoClaveInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> empleadoService.obtenerPorClave("BAD-1"));
    }
}
