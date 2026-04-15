package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmpleadoDepartamentoAssignmentIntegrationTest extends DepartamentosIntegrationTestBase {

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

        Departamento general = departamentoRepository.save(new Departamento(null, "General"));
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
                general));
    }

    @Test
    void crearEmpleadoConDepartamentoId_debeRetornar201() throws Exception {
        String payload = """
            {
              \"nombre\": \"Ana\",
              \"direccion\": \"Av 1\",
              \"telefono\": \"555\",
              \"correo\": \"ana.dep@test.com\",
              \"password\": \"clave123\",
              \"departamentoId\": %d
            }
            """.formatted(ventasId);

        mockMvc.perform(post("/api/v1/empleados")
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }
}
