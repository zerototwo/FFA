package com.isep.ffa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        // Public endpoints
        .antMatchers("/ffaAPI/public/**").permitAll()
        .antMatchers("/ffaAPI/auth/login").permitAll()
        .antMatchers("/ffaAPI/auth/register").permitAll()
        .antMatchers("/ffaAPI/auth/forgot-password").permitAll()
        .antMatchers("/ffaAPI/auth/reset-password").permitAll()
        .antMatchers("/ffaAPI/auth/verify-email").permitAll()
        .antMatchers("/ffaAPI/auth/resend-verification").permitAll()

        // Swagger endpoints
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/v3/api-docs/**").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("/webjars/**").permitAll()

        // Health check endpoints
        .antMatchers("/ffaAPI/health").permitAll()
        .antMatchers("/ffaAPI/info").permitAll()
        .antMatchers("/ffaAPI/endpoints").permitAll()
        .antMatchers("/ffaAPI/docs").permitAll()

        // Admin endpoints - require ADMIN role
        .antMatchers("/ffaAPI/admin/**").hasRole("ADMIN")

        // Intervener endpoints - require INTERVENER role
        .antMatchers("/ffaAPI/intervener/**").hasRole("INTERVENER")

        // User endpoints - require USER role
        .antMatchers("/ffaAPI/user/**").hasAnyRole("USER", "INTERVENER", "ADMIN")

        // Auth endpoints - require authentication
        .antMatchers("/ffaAPI/auth/**").authenticated()

        // All other requests need authentication
        .anyRequest().authenticated();

    // Add JWT filter
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
