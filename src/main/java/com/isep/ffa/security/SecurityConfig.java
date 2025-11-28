package com.isep.ffa.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.isep.ffa.service.PersonService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security Configuration
 * Configures security settings for the application
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint unauthorizedHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final PersonService personService;
  private final PasswordEncoder passwordEncoder;
  private final Environment environment;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz -> authz
            // Allow Swagger endpoints (Spring Security handles context path automatically)
            // But we also explicitly include context path patterns for Render compatibility
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-resources/**",
                "/webjars/**",
                "/ffaAPI/swagger-ui/**",
                "/ffaAPI/swagger-ui.html",
                "/ffaAPI/swagger-ui/index.html",
                "/ffaAPI/v3/api-docs/**",
                "/ffaAPI/v3/api-docs",
                "/ffaAPI/swagger-resources/**",
                "/ffaAPI/webjars/**")
            .permitAll()

            // Public and auth endpoints
            .requestMatchers("/public/**", "/auth/**").permitAll()

            // Other requests require authentication
            .anyRequest().authenticated())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(personService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http
        .getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(personService).passwordEncoder(passwordEncoder);
    return authenticationManagerBuilder.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    String allowedOriginsProperty = environment.getProperty("app.cors.allowed-origins", "*");

    // Parse allowed origins
    List<String> allowedOrigins = Arrays.stream(allowedOriginsProperty.split(","))
        .map(String::trim)
        .filter(origin -> !origin.isEmpty())
        .collect(Collectors.toList());

    // Use allowedOriginPatterns for flexibility (supports wildcards and
    // credentials)
    if (allowedOrigins.contains("*")) {
      // If wildcard is specified, allow all origins
      configuration.addAllowedOriginPattern("*");
    } else {
      // Add specific origins
      configuration.setAllowedOriginPatterns(allowedOrigins);
      // Also allow same-origin requests (for Swagger UI on same server)
      configuration.addAllowedOriginPattern("http://*");
      configuration.addAllowedOriginPattern("https://*");
    }

    // Allowed HTTP methods
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

    // Allowed request headers - allow all for Swagger UI compatibility
    configuration.setAllowedHeaders(Arrays.asList("*"));

    // Exposed response headers
    configuration.setExposedHeaders(Arrays.asList(
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Methods"));

    // Allow credentials (needed for JWT authentication)
    configuration.setAllowCredentials(true);

    // Cache duration for pre-flight requests (seconds)
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}