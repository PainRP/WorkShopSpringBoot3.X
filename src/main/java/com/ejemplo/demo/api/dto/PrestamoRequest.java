package com.ejemplo.demo.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PrestamoRequest(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        @NotNull(message = "La tasaAnual es obligatoria")
        @DecimalMin(value = "0.01", message = "La tasaAnual debe ser mayor a 0")
        BigDecimal tasaAnual,

        @NotNull(message = "Los meses son obligatorios")
        @Min(value = 1, message = "Los meses deben ser mayor o igual a 1")
        @Max(value = 360, message = "Los meses deben ser menor o igual a 360")
        Integer meses
) {
}
