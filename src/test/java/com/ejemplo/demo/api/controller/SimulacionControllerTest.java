package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.domain.service.PrestamoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulacionController.class)
class SimulacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @Test
    @DisplayName("Debe simular prestamo correctamente")
    void debeSimularPrestamoCorrectamente() throws Exception {
        PrestamoResponse response = new PrestamoResponse(
                BigDecimal.valueOf(1066.19),
                BigDecimal.valueOf(127.14),
                BigDecimal.valueOf(10127.14)
        );
        when(prestamoService.simular(any(PrestamoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "monto": 10000,
                                  "tasaAnual": 12,
                                  "meses": 12
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuotaMensual").value(1066.19));
    }

    @Test
    @DisplayName("Debe validar rango de meses")
    void debeValidarRangoDeMeses() throws Exception {
        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "monto": 10000,
                                  "tasaAnual": 12,
                                  "meses": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("Debe responder error de negocio cuando tasa supera maximo")
    void debeResponderErrorDeNegocioCuandoTasaSuperaMaximo() throws Exception {
        when(prestamoService.simular(any(PrestamoRequest.class)))
                .thenThrow(new IllegalArgumentException("La tasaAnual no puede ser mayor al 100%"));

        mockMvc.perform(post("/api/v1/simulaciones/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "monto": 10000,
                                  "tasaAnual": 120,
                                  "meses": 12
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("BUSINESS_RULE_ERROR"));
    }
}
