package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
class EmpleadoCrudAdminAuthorizationIntegrationTest extends IntegrationTestBase {

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
    void adminPuedeCrearActualizarYEliminar() throws Exception {
        String createPayload = """
                {
                  \"nombre\": \"Ana\",
                  \"direccion\": \"Av 1\",
                  \"telefono\": \"555\",
                  \"correo\": \"ana2@test.com\",
                  \"password\": \"clave123\",
                  \"departamentoId\": 1
                }
                """;

        mockMvc.perform(post("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated());

        String updatePayload = """
                {
                  \"nombre\": \"Usuario\",
                  \"direccion\": \"Dir actualizada\",
                  \"telefono\": \"222\",
                  \"correo\": \"usuario@test.com\",
                  \"departamentoId\": 1
                }
                """;

        mockMvc.perform(put("/api/v1/empleados/EMP-2")
                        .header("Authorization", BasicAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/empleados/EMP-2")
                        .header("Authorization", BasicAuthTestUtils.authHeader("admin@test.com", "clave123")))
                .andExpect(status().isNoContent());
    }
}
