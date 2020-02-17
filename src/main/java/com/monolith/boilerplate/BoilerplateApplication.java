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
	// TODO: Email service
	// TODO: Password Reset
	// TODO: Verification Token service implementation
	// TODO: LocalDateTime, locale setting.
	// TODO: JpaRepository vs CrudRepository?

	// TODO PROBLEMS
	// TODO: - Child object is not being removed.
	// TODO: - Fetching creates either infinitive loop or lazy initilatization error.
	// TODO: - Buy Hibernate premium course.
}
