package com.isep.ffa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Exclude unnecessary auto-configurations for faster startup
// Note: Exclusions can be done via application.yaml or by using
// @SpringBootApplication(exclude = {...})
// For now, we'll rely on application.yaml configuration for exclusions
@OpenAPIDefinition(info = @Info(title = "FFA Platform API", version = "1.0.0", description = "FFA Platform - Embassy Cooperation Projects Management System", contact = @Contact(name = "FFA Development Team", email = "ffa@isep.fr", url = "https://github.com/Maurras/PFP_2425_FFAPlatform_BE"), license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")))
public class FfaApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FfaApplication.class);
		// Disable banner for faster startup
		app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
		// Disable lazy initialization to avoid Swagger 500 errors
		// Swagger needs all controllers initialized to generate API docs
		app.setLazyInitialization(false);
		app.run(args);
	}
}
