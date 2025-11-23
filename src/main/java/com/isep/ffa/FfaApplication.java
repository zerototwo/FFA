package com.isep.ffa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "FFA Platform API", version = "1.0.0", description = "FFA Platform - Embassy Cooperation Projects Management System", contact = @Contact(name = "FFA Development Team", email = "ffa@isep.fr", url = "https://github.com/Maurras/PFP_2425_FFAPlatform_BE"), license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")))
@ImportRuntimeHints(FfaApplication.FfaRuntimeHints.class)
public class FfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FfaApplication.class, args);
	}

	/**
	 * Runtime hints for AOT compilation
	 * Registers reflection hints for classes that need runtime access
	 */
	static class FfaRuntimeHints implements RuntimeHintsRegistrar {
		@Override
		public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {
			// Register hints for reflection on common classes
			hints.reflection().registerType(java.util.List.class);
			hints.reflection().registerType(java.util.Map.class);
			hints.reflection().registerType(java.util.Set.class);
		}
	}
}
