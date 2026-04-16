package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.domain.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulaciones")
public class SimulacionController {

    // SOLUCION RETO (paso 8): endpoint desafiante apoyado por una capa de servicio.
    private final PrestamoService prestamoService;

    public SimulacionController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/prestamo")
    public ResponseEntity<PrestamoResponse> simularPrestamo(@Valid @RequestBody PrestamoRequest request) {
        // SOLUCION RETO: simulacion de prestamo con validaciones de entrada.
        return ResponseEntity.ok(prestamoService.simular(request));
    }
}
