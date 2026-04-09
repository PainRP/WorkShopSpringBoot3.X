package com.ejemplo.demo.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(SaludoController.class)
class SaludoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe responder health del workshop")
    void debeResponderHealthDelWorkshop() throws Exception {
        mockMvc.perform(get("/api/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ok"));
    }

    @Test
    @DisplayName("Prueba de saludo exitoso")
    void pruebaSaludoExitoso() throws Exception{
    	mockMvc.perform(get("/api/v1/saludos")
    			.param("nombre","Ana"))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.mensaje").value("Hola Ana"));
    }
    
    @Test
    @DisplayName("2) POST /api/v1/saludos con {\"nombre\":\"\"} -> 400 y codigo VALIDATION_ERROR")
    void debeDarErrorSiNombreEstaVacio() throws Exception {
        mockMvc.perform(post("/api/v1/saludos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"\"}")) 
                .andExpect(status().isBadRequest()) 
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR"));
    }
}
