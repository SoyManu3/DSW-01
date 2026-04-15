package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmpleadoAuthenticationIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        empleadoRepository.deleteAll();
        departamentoRepository.deleteAll();
        Departamento departamento = departamentoRepository.save(new Departamento(null, "General"));
        Empleado empleado = new Empleado(
                new EmpleadoId("EMP", 1L),
                "Ana",
                "Dir",
                "555",
                "ana@test.com",
                passwordEncoder.encode("clave123"),
                true,
                0,
            null,
            departamento);
        empleadoRepository.save(empleado);
    }

    @Test
    void autenticacionValida_debePermitirAcceso() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("ana@test.com", "clave123")))
                .andExpect(status().isOk());
    }

    @Test
    void autenticacionInvalida_debeRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("ana@test.com", "incorrecta")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cuentaBloqueada_debeRetornar423() throws Exception {
        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase("ana@test.com").orElseThrow();
        empleado.setBloqueadoHasta(Instant.now().plusSeconds(600));
        empleado.setIntentosFallidos(5);
        empleadoRepository.save(empleado);

        mockMvc.perform(get("/api/v1/empleados")
                        .header("Authorization", BasicAuthTestUtils.authHeader("ana@test.com", "clave123")))
                .andExpect(status().isLocked());
    }

    @Test
    void autenticacionValida_p95DebeSerMenorA2SegundosEn10Ejecuciones() throws Exception {
        List<Long> duracionesMs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            long inicio = System.currentTimeMillis();
            mockMvc.perform(get("/api/v1/empleados")
                            .header("Authorization", BasicAuthTestUtils.authHeader("ana@test.com", "clave123")))
                    .andExpect(status().isOk());
            duracionesMs.add(System.currentTimeMillis() - inicio);
        }

        Collections.sort(duracionesMs);
        int indiceP95 = (int) Math.ceil(0.95 * duracionesMs.size()) - 1;
        long p95 = duracionesMs.get(Math.max(indiceP95, 0));

        org.junit.jupiter.api.Assertions.assertTrue(
                p95 < 2000,
                "La latencia p95 de autenticación debe ser < 2000ms, valor actual=" + p95 + "ms");
    }
}
