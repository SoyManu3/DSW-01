package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DepartamentoSecurityIntegrationTest extends DepartamentosIntegrationTestBase {

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

        empleadoRepository.save(new Empleado(
                new EmpleadoId("EMP", 2L),
                "Usuario",
                "Dir",
                "222",
                "usuario@test.com",
                passwordEncoder.encode("clave123"),
                true,
                0,
                null,
                RolEmpleado.EMPLEADO,
                departamento));
    }

    @Test
    void empleadoPuedeLeerPeroNoEscribirDepartamentos() throws Exception {
        String payload = """
                {
                  \"nombre\": \"Ventas\"
                }
                """;

        mockMvc.perform(get("/api/v1/departamentos")
                        .header("Authorization", BasicAuthTestUtils.authHeader("usuario@test.com", "clave123")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/departamentos")
                        .header("Authorization", BasicAuthTestUtils.authHeader("usuario@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/v1/departamentos/1")
                        .header("Authorization", BasicAuthTestUtils.authHeader("usuario@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/v1/departamentos/1")
                        .header("Authorization", BasicAuthTestUtils.authHeader("usuario@test.com", "clave123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPuedeEscribirDepartamentos() throws Exception {
        String payload = """
                {
                  \"nombre\": \"Operaciones\"
                }
                """;

        mockMvc.perform(post("/api/v1/departamentos")
                        .header("Authorization", BasicAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }
}
