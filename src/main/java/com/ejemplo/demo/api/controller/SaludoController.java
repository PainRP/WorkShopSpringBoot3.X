package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.dto.SaludoRequest;
import com.ejemplo.demo.api.dto.SaludoResponse;
import com.ejemplo.demo.domain.service.SaludoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SaludoController {

    // SOLUCION RETO (pasos 2 y 3): inyeccion por constructor para usar la logica de saludo.
    private final SaludoService saludoService;

    public SaludoController(SaludoService saludoService) {
        this.saludoService = saludoService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "estado", "ok",
                "mensaje", "Workshop Spring Boot activo"
        ));
    }

    @GetMapping("/saludos")
    public ResponseEntity<SaludoResponse> saludar(
            @RequestParam(defaultValue = "Mundo") String nombre
    ) {
        // SOLUCION RETO: endpoint GET habilitado.
        return ResponseEntity.ok(saludoService.crearSaludo(nombre));
    }

    @PostMapping("/saludos")
    public ResponseEntity<SaludoResponse> saludarPost(@Valid @RequestBody SaludoRequest request) {
        // SOLUCION RETO: endpoint POST con validacion habilitado.
        return ResponseEntity.ok(saludoService.crearSaludo(request.nombre()));
    }
}
