package com.valor.scamshield_service.config;


import com.valor.scamshield_service.ScamshieldServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import static org.assertj.core.api.Assertions.assertThat;

class OpenAiPropertiesValidationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(ScamshieldServiceApplication.class);

    @Test
    void contextLoadsWhenApiKeyIsProvided() {
        contextRunner
                .withPropertyValues("openai.api-key=test-key-123")
                .run(context -> {
                    assertThat(context).hasSingleBean(OpenAiProperties.class);
                    assertThat(context.getBean(OpenAiProperties.class).apiKey()).isEqualTo("test-key-123");
                });
    }
}