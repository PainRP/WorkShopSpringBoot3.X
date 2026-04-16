package com.ejemplo.demo.domain.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EstadoSingletonService {

    // Se usa AtomicInteger para mostrar estado compartido de forma segura.
    private final AtomicInteger ultimoValor = new AtomicInteger(0);

    public int actualizar(int valor) {
        ultimoValor.set(valor);
        return ultimoValor.get();
    }

    public int obtenerActual() {
        return ultimoValor.get();
    }

    public int reiniciar() {
        ultimoValor.set(0);
        return ultimoValor.get();
    }
}
