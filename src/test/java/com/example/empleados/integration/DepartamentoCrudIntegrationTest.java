package com.example.empleados.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class DepartamentoCrudIntegrationTest extends DepartamentosIntegrationTestBase {

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
                departamento));
    }

    @Test
    void crudCompletoDepartamento_debeFuncionar() throws Exception {
        String payloadCreate = """
            {
              \"nombre\": \"Ventas\"
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/v1/departamentos")
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadCreate))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Ventas"))
                .andReturn();

        String content = createResult.getResponse().getContentAsString();
        long id = Long.parseLong(content.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));

        mockMvc.perform(get("/api/v1/departamentos/" + id)
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id));

        String payloadUpdate = """
            {
              \"nombre\": \"Comercial\"
            }
            """;

        mockMvc.perform(put("/api/v1/departamentos/" + id)
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Comercial"));

        mockMvc.perform(delete("/api/v1/departamentos/" + id)
                        .header("Authorization", DepartamentosAuthTestUtils.authHeader("admin@test.com", "clave123")))
                .andExpect(status().isNoContent());
    }
}
