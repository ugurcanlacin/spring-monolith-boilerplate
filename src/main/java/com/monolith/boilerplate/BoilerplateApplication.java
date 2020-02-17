package com.monolith.boilerplate;

import com.monolith.boilerplate.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BoilerplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoilerplateApplication.class, args);
	}
	// TODO: Password Reset
	// TODO: Verification Token implementation
	// TODO: Change Date to LocalDateTime
}
