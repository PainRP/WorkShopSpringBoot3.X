package com.ejemplo.demo.domain.service;

import com.ejemplo.demo.api.dto.PrestamoRequest;
import com.ejemplo.demo.api.dto.PrestamoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class PrestamoService {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final BigDecimal DOCE = BigDecimal.valueOf(12);
    private static final MathContext MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);

    public PrestamoResponse simular(PrestamoRequest request) {
        // SOLUCION RETO (paso 8): validacion de reglas previas al calculo financiero.
        validarReglas(request);

        BigDecimal monto = request.monto();
        int meses = request.meses();
        BigDecimal tasaMensual = request.tasaAnual().divide(CIEN, MATH_CONTEXT).divide(DOCE, MATH_CONTEXT);

        // SOLUCION RETO: formula de cuota fija (amortizacion) solicitada en la actividad.
        double factor = Math.pow(BigDecimal.ONE.add(tasaMensual).doubleValue(), meses);
        BigDecimal numerador = monto.multiply(tasaMensual, MATH_CONTEXT).multiply(BigDecimal.valueOf(factor), MATH_CONTEXT);
        BigDecimal denominador = BigDecimal.valueOf(factor).subtract(BigDecimal.ONE, MATH_CONTEXT);
        BigDecimal cuotaMensual = numerador.divide(denominador, 2, RoundingMode.HALF_UP);

        BigDecimal totalPagar = cuotaMensual.multiply(BigDecimal.valueOf(meses)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesTotal = totalPagar.subtract(monto).setScale(2, RoundingMode.HALF_UP);

        return new PrestamoResponse(cuotaMensual, interesTotal, totalPagar);
    }

    private void validarReglas(PrestamoRequest request) {
        // SOLUCION RETO: reglas de negocio adicionales (aparte de @Valid en DTO).
        if (request.tasaAnual().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("La tasaAnual no puede ser mayor al 100%");
        }
        if (request.monto().compareTo(BigDecimal.valueOf(5_000_000)) > 0) {
            throw new IllegalArgumentException("El monto excede el maximo permitido para simulacion");
        }
    }
}
