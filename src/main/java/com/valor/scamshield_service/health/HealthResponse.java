package com.valor.scamshield_service.health;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {
    private String status;
    private String version;
    private long uptimeSeconds;
}
