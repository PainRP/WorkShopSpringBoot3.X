package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
 
@Service
public class SimuladorService {

    public PrestamoResponse simularPrestamo(PrestamoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos de la solicitud no pueden ser nulos");
        }

        double p = request.monto().doubleValue();
        double tasaAnual = request.tasaAnual().doubleValue();
        int n = request.meses();

        double r = tasaAnual / 12 / 100;
        double cuota;

        if (r == 0) {
            cuota = p / n; 
        } else {
            
            double factor = Math.pow(1 + r, n);
            cuota = p * (r * factor) / (factor - 1);
        }

        double totalPagar = cuota * n;
        double interesTotal = totalPagar - p;

        return new PrestamoResponse(
                BigDecimal.valueOf(cuota).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(interesTotal).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(totalPagar).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
