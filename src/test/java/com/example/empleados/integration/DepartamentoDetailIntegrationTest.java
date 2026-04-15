package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DepartamentoDetailIntegrationTest extends DepartamentosIntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long ventasId;

    @BeforeEach
    void init() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();

        Departamento ventas = departamentoRepository.save(new Departamento(null, "Ventas"));
        ventasId = ventas.getId();

        empleadoRepository.save(new Empleado(
                new EmpleadoId("EMP", 1L),
                "Admin",
                "Dir",
                "111",
                "admin@test.com",
                passwordEncoder.encode("clave123"),
                true,
                0,
                null,
                ventas));

        empleadoRepository.save(new Empleado(
                new EmpleadoId("EMP", 2L),
                "Ana",
                "Dir2",
                "222",
                "ana@test.com",
                passwordEncoder.encode("clave123"),
                true,
                0,
                null,
                ventas));
    }

    @Test
    void detalleDepartamento_debeRetornarEmpleadosCount() throws Exception {
        mockMvc.perform(get("/api/v1/departamentos/" + ventasId)
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ventasId.intValue()))
                .andExpect(jsonPath("$.empleadosCount").value(2));
    }
}
