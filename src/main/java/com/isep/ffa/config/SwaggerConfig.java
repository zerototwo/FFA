package com.isep.ffa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@org.springframework.context.annotation.Lazy(false) // Disable lazy loading for Swagger to avoid 500 errors
public class SwaggerConfig {

        @Value("${app.api-base-urls:http://localhost:8080/ffaAPI}")
        private String apiBaseUrls;

        @Bean
        public OpenAPI customOpenAPI() {
                // Safely parse API base URLs with error handling
                List<Server> servers;
                try {
                        servers = Arrays.stream(apiBaseUrls.split(","))
                                        .map(String::trim)
                                        .filter(StringUtils::isNotBlank)
                                        .map(url -> new Server()
                                                        .url(url)
                                                        .description(url.contains("localhost") ? "Development Server"
                                                                        : "Production Server"))
                                        .collect(Collectors.toList());
                } catch (Exception e) {
                        // Fallback to default server if parsing fails
                        servers = Arrays.asList(new Server()
                                        .url("http://localhost:8080/ffaAPI")
                                        .description("Default Server"));
                }

                return new OpenAPI()
                                .info(new Info()
                                                .title("FFA Platform API")
                                                .description("FFA Platform - Embassy Cooperation Projects Management System. "
                                                                +
                                                                "To use protected endpoints, first authenticate via /auth/login to get a JWT token, "
                                                                +
                                                                "then click the 'Authorize' button above and enter: Bearer <your-token>")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("FFA Development Team")
                                                                .email("ffa@isep.fr")
                                                                .url("https://github.com/Maurras/PFP_2425_FFAPlatform_BE"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(servers)
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearer-jwt",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .name("Bearer Authentication")
                                                                                .description("Enter your JWT token. You can get it by calling /auth/login endpoint. Format: Bearer <your-token>")))
                                .tags(Arrays.asList(
                                                new Tag().name("Authentication")
                                                                .description("User authentication and authorization"),
                                                new Tag().name("User Management")
                                                                .description("User account management"),
                                                new Tag().name("Country Management")
                                                                .description("Country information management"),
                                                new Tag().name("Embassy Management")
                                                                .description("Embassy information management"),
                                                new Tag().name("Project Management")
                                                                .description("Project creation and management"),
                                                new Tag().name("Application Management")
                                                                .description("Project application management"),
                                                new Tag().name("Message Management")
                                                                .description("Message and notification management"),
                                                new Tag().name("Admin")
                                                                .description("Administrative functions")));
        }
}