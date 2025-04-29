package com.valor.scamshield_service.analyze;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeResponse {
    private int score;
    private String summary;
}
