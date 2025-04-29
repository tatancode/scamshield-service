package com.valor.scamshield_service.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
    private static final String API_VERSION = "0.0.1-SNAPSHOT";

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth(){
        HealthResponse healthResponse = new HealthResponse("ok",API_VERSION, 0L);
        return ResponseEntity.ok(healthResponse);
    }
}
