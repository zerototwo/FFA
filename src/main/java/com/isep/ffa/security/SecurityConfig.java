package com.isep.ffa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security Configuration
 * Configures security settings for the application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz -> authz
            // 允许所有Swagger相关端点
            .requestMatchers("/ffaAPI/swagger-ui/**").permitAll()
            .requestMatchers("/ffaAPI/v3/api-docs/**").permitAll()
            .requestMatchers("/ffaAPI/swagger-resources/**").permitAll()
            .requestMatchers("/ffaAPI/webjars/**").permitAll()
            .requestMatchers("/ffaAPI/swagger-ui.html").permitAll()

            // 允许所有其他请求（临时）
            .anyRequest().permitAll());

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 允许的源
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "*",
        "http://localhost:*",
        "https://localhost:*",
        "http://127.0.0.1:*",
        "https://127.0.0.1:*",
        "https://ffa-api.isep.fr",
        "http://ffa-api.isep.fr"));

    // 允许的HTTP方法
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

    // 允许的请求头
    configuration.setAllowedHeaders(Arrays.asList(
        "*",
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "Accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"));

    // 允许的响应头
    configuration.setExposedHeaders(Arrays.asList(
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Methods"));

    // 允许发送凭证
    configuration.setAllowCredentials(true);

    // 预检请求的缓存时间（秒）
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}