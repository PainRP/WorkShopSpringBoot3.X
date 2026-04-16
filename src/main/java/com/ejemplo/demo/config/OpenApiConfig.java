package com.ejemplo.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        // SOLUCION RETO (paso 7): metadata de Swagger/OpenAPI para documentar la API.
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot API Demo Progra 3")
                        .version("v1.0.0")
                        .description("API de referencia para taller Spring Boot")
                        .contact(new Contact()
                                .name("Progra 3")
                                .email("docente@umg.edu.gt")));
    }
}
