package com.valor.scamshield_service.analyze;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class AnalyzeController {
    @GetMapping("/report")
    public ResponseEntity<ReportResponse> postScam(){
        ReportResponse reportResponse = new ReportResponse("summary", "recommendation");
        return ResponseEntity.ok(reportResponse);
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        AnalyzeResponse dummyResponse = new AnalyzeResponse(0, "dummy summary - not implemented");
        return ResponseEntity.ok(dummyResponse);
    }
}
