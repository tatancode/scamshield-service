package com.valor.scamshield_service;

import com.valor.scamshield_service.config.OpenAiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenAiProperties.class)
public class ScamshieldServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScamshieldServiceApplication.class, args);
	}

}
