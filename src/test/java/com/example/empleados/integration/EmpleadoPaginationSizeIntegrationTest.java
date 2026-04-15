package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.domain.RolEmpleado;
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
class EmpleadoPaginationSizeIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();

        Departamento departamento = departamentoRepository.save(new Departamento(null, "General"));

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
                RolEmpleado.ADMIN,
                departamento));

        for (int i = 2; i <= 13; i++) {
            empleadoRepository.save(new Empleado(
                    new EmpleadoId("EMP", (long) i),
                    "Empleado " + i,
                    "Dir",
                    "111",
                    "empleado" + i + "@test.com",
                    passwordEncoder.encode("clave123"),
                    true,
                    0,
                    null,
                    RolEmpleado.EMPLEADO,
                    departamento));
        }
    }

    @Test
    void listadoDebeRetornarTamanioFijoDe10() throws Exception {
        mockMvc.perform(get("/api/v1/empleados?page=0")
                        .header("Authorization", BasicAuthTestUtils.authHeader("admin@test.com", "clave123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content.length()").value(10));
    }
}
