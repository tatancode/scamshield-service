package com.valor.scamshield_service.aiwrapper;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiRequest {
    @NotBlank(message = "model can't be blank")
    String model;

    @NotBlank(message = "input can't be blank")
    String input;
}
