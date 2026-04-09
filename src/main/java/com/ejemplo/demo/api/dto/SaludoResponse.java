package com.ejemplo.demo.api.dto;

import java.time.Instant;

public record SaludoResponse(
        String mensaje,
        Instant timestamp
) {
}
