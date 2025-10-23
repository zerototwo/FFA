package com.isep.ffa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("FFA Platform API")
                                                .description("FFA Platform - Embassy Cooperation Projects Management System")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("FFA Development Team")
                                                                .email("ffa@isep.fr")
                                                                .url("https://github.com/Maurras/PFP_2425_FFAPlatform_BE"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(Arrays.asList(
                                                new Server()
                                                                .url("http://localhost:8080/ffaAPI")
                                                                .description("Development Server"),
                                                new Server()
                                                                .url("https://ffa-api.isep.fr/ffaAPI")
                                                                .description("Production Server")))
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