package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.SaludoResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SaludoService {

    public SaludoResponse crearSaludo(String nombre) {
        String nombreNormalizado = normalizarNombre(nombre);
        String mensaje = "Hola, %s. Bienvenido a Spring Boot 3!".formatted(nombreNormalizado);
        return new SaludoResponse(mensaje, Instant.now());
    }

    String normalizarNombre(String nombre) {
    	String temp = nombre;
    	nombre= temp.toUpperCase().charAt(0) + temp.substring(1, temp.length()).toLowerCase();
    	nombre.replace(" ","");
        return nombre == null ? "Mundo" : nombre.trim();

    }
}
