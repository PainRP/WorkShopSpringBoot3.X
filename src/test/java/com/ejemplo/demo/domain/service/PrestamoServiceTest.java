package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrestamoServiceTest {

    private final PrestamoService prestamoService = new PrestamoService();

    @Test
    @DisplayName("Debe calcular simulacion de prestamo")
    void debeCalcularSimulacionDePrestamo() {
        PrestamoRequest request = new PrestamoRequest(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(12),
                12
        );

        PrestamoResponse response = prestamoService.simular(request);

        assertEquals(BigDecimal.valueOf(888.49), response.cuotaMensual());
        assertEquals(BigDecimal.valueOf(661.88), response.interesTotal());
        assertEquals(BigDecimal.valueOf(10661.88), response.totalPagar());
    }

    @Test
    @DisplayName("Debe fallar cuando tasa es mayor a 100")
    void debeFallarCuandoTasaEsMayorACien() {
        PrestamoRequest request = new PrestamoRequest(
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(120),
                12
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> prestamoService.simular(request));
        assertEquals("La tasaAnual no puede ser mayor al 100%", ex.getMessage());
    }
}
