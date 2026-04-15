package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import java.time.Instant;
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
class EmpleadoAuthorizationIntegrationTest extends IntegrationTestBase {

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
                new EmpleadoId("EMP", 2L),
                "Luis",
                "Dir",
                "444",
                "luis@test.com",
                passwordEncoder.encode("clave123"),
                true,
                0,
                null,
                departamento));
    }

    @Test
    void sinCredenciales_debeRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/empleados"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/empleados"));
    }

    @Test
    void conCredencialesValidas_debeRetornar200() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("luis@test.com", "clave123")))
                .andExpect(status().isOk());
    }

    @Test
    void cuentaBloqueada_debeRetornar423ConApiError() throws Exception {
        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase("luis@test.com").orElseThrow();
        empleado.setBloqueadoHasta(Instant.now().plusSeconds(600));
        empleado.setIntentosFallidos(5);
        empleadoRepository.save(empleado);

        mockMvc.perform(get("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("luis@test.com", "clave123")))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(423))
                .andExpect(jsonPath("$.error").value("Locked"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/empleados"));
    }

    @Test
    void creacionConPayloadInvalido_debeRetornar400ConApiError() throws Exception {
        String payloadInvalido = """
            {
              \"nombre\": \"\",
              \"direccion\": \"Dir\",
              \"telefono\": \"123\",
              \"correo\": \"correo-invalido\",
              \"password\": \"123\",
              \"departamentoId\": 1
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("luis@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/empleados"));
    }

    @Test
    void creacionConCorreoDuplicado_debeRetornar409ConApiError() throws Exception {
        String payloadDuplicado = """
            {
              \"nombre\": \"Otro\",
              \"direccion\": \"Dir\",
              \"telefono\": \"123\",
              \"correo\": \"luis@test.com\",
              \"password\": \"clave123\",
              \"departamentoId\": 1
            }
            """;

        mockMvc.perform(post("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("luis@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDuplicado))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/api/v1/empleados"));
    }
}
