package com.isep.ffa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database Configuration Class
 * Supports both JPA and MyBatis-Plus
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.isep.ffa.repository")
@EnableTransactionManagement
public class DatabaseConfig {
  // Database configuration is handled by application.yaml
  // This class enables JPA repositories and transaction management
  // MyBatis-Plus configuration is handled by MybatisPlusConfig.java
}
