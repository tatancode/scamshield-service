package com.valor.scamshield_service.analyze;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalyzeRequest {
    @NotBlank(message = "Message cannot be blank")
    private String message;
}
