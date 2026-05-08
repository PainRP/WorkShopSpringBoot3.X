package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.contract.SimulacionesApi;
import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import com.ejemplo.demo.domain.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimulacionController implements SimulacionesApi {

    private final PrestamoService prestamoService;

    public SimulacionController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @Override
    public ResponseEntity<PrestamoResponse> simularPrestamo(PrestamoRequest request) {
        return ResponseEntity.ok(prestamoService.simular(request));
    }
}