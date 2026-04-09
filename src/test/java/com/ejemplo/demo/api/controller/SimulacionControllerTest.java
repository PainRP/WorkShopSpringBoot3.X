package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.domain.service.SimuladorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
 
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulacionController.class)
class SimulacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
    @MockBean
    private SimuladorService simuladorService;

    @Test
    @DisplayName("1) POST /api/v1/simulaciones/prestamo con datos válidos -> 200 OK y cálculo correcto")
    void casoExitoso() throws Exception {
        
        PrestamoResponse mockResponse = new PrestamoResponse(
                new BigDecimal("888.49"), 
                new BigDecimal("661.85"), 
                new BigDecimal("10661.85")
        );
        Mockito.when(simuladorService.simularPrestamo(any())).thenReturn(mockResponse);

        String jsonRequest = """
                {
                  "monto": 10000,
                  "tasaAnual": 12.0,
                  "meses": 12
                }
                """;

        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuotaMensual").value(888.49))
                .andExpect(jsonPath("$.interesTotal").value(661.85))
                .andExpect(jsonPath("$.totalPagar").value(10661.85));
    }

    @Test
    @DisplayName("2) POST /api/v1/simulaciones/prestamo con meses inválidos (>360) -> 400 Bad Request")
    void casoInvalidoPorMeses() throws Exception {
        String jsonRequest = """
                {
                  "monto": 10000,
                  "tasaAnual": 12.0,
                  "meses": 400
                }
                """; 
        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR")) // Utilizando tu GlobalExceptionHandler
                .andExpect(jsonPath("$.detalles.meses").exists());
    }
}