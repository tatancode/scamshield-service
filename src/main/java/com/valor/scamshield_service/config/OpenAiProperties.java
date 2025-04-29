package com.valor.scamshield_service.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for the OpenAI service.
 * Binds to properties prefixed with "openai".
 *
 * @param apiKey The API key for authenticating with OpenAI. Must not be blank.
 */
@ConfigurationProperties("openai")
@Validated
public record OpenAiProperties (
        @NotBlank(message = "OpenAI API key must not be blank")
        String apiKey
){}
