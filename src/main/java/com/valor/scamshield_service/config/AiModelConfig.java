package com.valor.scamshield_service.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "models.openai")
@Validated
@Data
public class AiModelConfig {
    @NotBlank(message="Endpoint must not be blank")
    String endpoint;

    @NotBlank(message="apiKey must not be blank")
    String apiKey;
}
