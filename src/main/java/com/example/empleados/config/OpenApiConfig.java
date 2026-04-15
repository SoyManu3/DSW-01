package com.example.empleados.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI empleadosOpenApi() {
        String schemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API Empleados con Autenticación por Correo")
                        .version("1.2.0")
                        .description("Contrato de API para gestión de empleados protegida con HTTP Basic Auth."))
                .components(new Components()
                        .addSecuritySchemes(schemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName));
    }
}
