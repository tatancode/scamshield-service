package com.valor.scamshield_service.analyze;

import com.valor.scamshield_service.aiwrapper.AiResponse;
import com.valor.scamshield_service.aiwrapper.AiService;
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
    private final AiService aiService;

    public AnalyzeController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AiResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        AiResponse response = aiService.analyzeMessage(request);
        System.out.println("aiResponse: " + response);
        return ResponseEntity.ok(response);
    }
}
