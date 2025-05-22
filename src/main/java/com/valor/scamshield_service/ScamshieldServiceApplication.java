package com.valor.scamshield_service;

import com.valor.scamshield_service.config.AiModelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AiModelConfig.class)
public class ScamshieldServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScamshieldServiceApplication.class, args);
	}

}
