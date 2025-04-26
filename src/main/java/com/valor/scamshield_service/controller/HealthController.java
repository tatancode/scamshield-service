package com.valor.scamshield_service.controller;

import com.valor.scamshield_service.response.HealthResponse;
import com.valor.scamshield_service.response.ReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.System.currentTimeMillis;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
    private static final String API_VERSION = "0.0.1-SNAPSHOT";

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth(){
        HealthResponse healthResponse = new HealthResponse("ok",API_VERSION, 0L);
        return ResponseEntity.ok(healthResponse);
    }

    @GetMapping("/report")
    public ResponseEntity<ReportResponse> postScam(){
        ReportResponse reportResponse = new ReportResponse("summary", "recommendation");
        return ResponseEntity.ok(reportResponse);
    }

    @PostMapping("/analysis")
    public ResponseEntity<ReportResponse> postAnalysis(){
        ReportResponse reportResponse = new ReportResponse("summary", "recommendation");
        return ResponseEntity.ok(reportResponse);
    }
}
