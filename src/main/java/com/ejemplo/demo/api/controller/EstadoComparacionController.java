package com.ejemplo.demo.api.controller;

import com.ejemplo.demo.api.contract.DemoEstadoApi;
import com.ejemplo.demo.api.dto.EstadoResponse;
import com.ejemplo.demo.domain.model.EstadoManual;
import com.ejemplo.demo.domain.service.EstadoSingletonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstadoComparacionController implements DemoEstadoApi {

    private final EstadoSingletonService estadoSingletonService;

    public EstadoComparacionController(EstadoSingletonService estadoSingletonService) {
        this.estadoSingletonService = estadoSingletonService;
    }

    @Override
    public ResponseEntity<EstadoResponse> actualizarSingleton(Integer valor) {
        int actual = estadoSingletonService.actualizar(valor);
        return ResponseEntity.ok(new EstadoResponse("singleton", actual));
    }

    @Override
    public ResponseEntity<EstadoResponse> obtenerSingleton() {
        int actual = estadoSingletonService.obtenerActual();
        return ResponseEntity.ok(new EstadoResponse("singleton", actual));
    }

    @Override
    public ResponseEntity<EstadoResponse> reiniciarSingleton() {
        int actual = estadoSingletonService.reiniciar();
        return ResponseEntity.ok(new EstadoResponse("singleton", actual));
    }

    @Override
    public ResponseEntity<EstadoResponse> actualizarManual(Integer valor) {
        EstadoManual estadoManual = new EstadoManual();
        estadoManual.setValor(valor);
        return ResponseEntity.ok(new EstadoResponse("manual", estadoManual.getValor()));
    }

    @Override
    public ResponseEntity<EstadoResponse> obtenerManual() {
        EstadoManual estadoManual = new EstadoManual();
        return ResponseEntity.ok(new EstadoResponse("manual", estadoManual.getValor()));
    }
}
