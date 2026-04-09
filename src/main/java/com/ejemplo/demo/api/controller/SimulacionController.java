package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.domain.service.SimuladorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulaciones")
@Tag(name = "Simulaciones", description = "Endpoints para simulaciones financieras")
public class SimulacionController {

    private final SimuladorService simuladorService;

    public SimulacionController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @PostMapping("/prestamo")
    @Operation(summary = "Simular Préstamo", description = "Calcula la cuota mensual, el interés total y el total a pagar utilizando el sistema de amortización de cuota fija.")
    public ResponseEntity<PrestamoResponse> simularPrestamo(
            @Valid @RequestBody PrestamoRequest request) {
        
        PrestamoResponse response = simuladorService.simularPrestamo(request);
        return ResponseEntity.ok(response);
    }
}
