package com.ejemplo.demo.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EstadoComparacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Singleton debe conservar valor entre llamadas")
    void singletonDebeConservarValorEntreLlamadas() throws Exception {
        mockMvc.perform(post("/api/v1/demo/estado/singleton/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorActual").value(0));

        mockMvc.perform(post("/api/v1/demo/estado/singleton/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorActual").value(25));

        mockMvc.perform(get("/api/v1/demo/estado/singleton"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorActual").value(25));
    }

    @Test
    @DisplayName("Manual sin Service debe reiniciarse en cada llamada")
    void manualSinServiceDebeReiniciarseEnCadaLlamada() throws Exception {
        mockMvc.perform(post("/api/v1/demo/estado/manual/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorActual").value(25));

        mockMvc.perform(get("/api/v1/demo/estado/manual"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorActual").value(0));
    }
}
