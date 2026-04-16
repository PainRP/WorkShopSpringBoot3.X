package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.domain.model.EstadoManual;
import com.ejemplo.demo.domain.service.EstadoSingletonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo/estado")
public class EstadoComparacionController {

    // SOLUCION EXTRA: comparacion didactica entre bean singleton y clase manual.
    private final EstadoSingletonService estadoSingletonService;

    public EstadoComparacionController(EstadoSingletonService estadoSingletonService) {
        this.estadoSingletonService = estadoSingletonService;
    }

    @PostMapping("/singleton/{valor}")
    public ResponseEntity<Map<String, Object>> actualizarSingleton(@PathVariable int valor) {
        // Bean singleton de Spring: el valor queda persistido para llamadas posteriores.
        int actual = estadoSingletonService.actualizar(valor);
        return ResponseEntity.ok(Map.of(
                "tipo", "singleton",
                "valorActual", actual
        ));
    }

    @GetMapping("/singleton")
    public ResponseEntity<Map<String, Object>> obtenerSingleton() {
        return ResponseEntity.ok(Map.of(
                "tipo", "singleton",
                "valorActual", estadoSingletonService.obtenerActual()
        ));
    }

    @PostMapping("/singleton/reset")
    public ResponseEntity<Map<String, Object>> reiniciarSingleton() {
        return ResponseEntity.ok(Map.of(
                "tipo", "singleton",
                "valorActual", estadoSingletonService.reiniciar()
        ));
    }

    //No singleton
    
    
    @PostMapping("/manual/{valor}")
    public ResponseEntity<Map<String, Object>> actualizarManual(@PathVariable int valor) {
        // Clase sin @Service: se crea con new y su estado no persiste entre requests.
        EstadoManual estadoManual = new EstadoManual();
        estadoManual.setValor(valor);
        return ResponseEntity.ok(Map.of(
                "tipo", "manual",
                "valorActual", estadoManual.getValor()
        ));
    }

    @GetMapping("/manual")
    public ResponseEntity<Map<String, Object>> obtenerManual() {
        // Cada llamada inicia en 0 porque es una instancia nueva.
        EstadoManual estadoManual = new EstadoManual();
        return ResponseEntity.ok(Map.of(
                "tipo", "manual",
                "valorActual", estadoManual.getValor()
        ));
    }
}
