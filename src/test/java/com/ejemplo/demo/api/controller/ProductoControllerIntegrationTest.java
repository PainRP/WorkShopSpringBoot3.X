package com.ejemplo.demo.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ejemplo.demo.domain.model.Categoria;
import com.ejemplo.demo.domain.repository.CategoriaRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Limpia la basura que deja cada test
@Testcontainers // Invoca a la constructora mágica de Docker
public class ProductoControllerIntegrationTest {

    // Levantamos un PostgreSQL efímero idéntico al de producción en un contenedor
    @Container
    @ServiceConnection 
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc; // Nuestro inspector de sanidad encubierto

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void alCrearProductoValido_deberiaRetornar201() throws Exception {
        // 1. Crear y guardar una categoría real para que el test funcione
        Categoria cat = new Categoria();
        cat.setNombre("Hardware");
        cat = categoriaRepository.save(cat);

        // 2. Usar el ID real de la categoría guardada
        String jsonValido = """
            {
                "nombre": "Gabinete RGB Test",
                "precio": 150.00,
                "categoriaId": %d
            }
        """.formatted(cat.getId());

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated());
    }

    @Test
    public void alEnviarDatosInvalidos_deberiaRetornar400() throws Exception {
        // Mandamos un precio negativo y un nombre vacío para que la validación falle
        String jsonInvalido = """
            {
                "nombre": "",
                "precio": -50.00
            }
        """;

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest()); // Esperamos un 400
    }

    @Test
    public void alBuscarProductoInexistente_deberiaRetornar404() throws Exception {
        // Buscamos un ID absurdo que no está en la base de datos temporal
        mockMvc.perform(get("/api/v1/productos/999999"))
                .andExpect(status().isNotFound()); // Esperamos un 404
    }
}