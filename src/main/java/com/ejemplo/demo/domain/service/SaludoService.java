package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.SaludoResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SaludoService {

    public SaludoResponse crearSaludo(String nombre) {
        // SOLUCION RETO (paso 4): se normaliza y valida el nombre antes de responder.
        String nombreNormalizado = normalizarNombre(nombre);
        String mensaje = "Hola, %s. Bienvenido a Spring Boot 3!".formatted(nombreNormalizado);
        return new SaludoResponse(mensaje, Instant.now());
    }

    String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "Mundo";
        }

        String limpio = nombre.trim();
        if (limpio.matches(".*\\d.*")) {
            // SOLUCION RETO: regla de negocio para forzar nombres sin numeros.
            throw new IllegalArgumentException("El nombre no puede contener numeros");
        }

        // SOLUCION RETO: primera letra mayuscula, resto minuscula.
        return limpio.substring(0, 1).toUpperCase() + limpio.substring(1).toLowerCase();
    }
}
