package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * API Information Controller
 * Provides API information and status endpoints
 * Base path: /ffaAPI
 */
@RestController
@RequestMapping("/ffaAPI")
@Tag(name = "API Information", description = "API information and status endpoints")
public class ApiInfoController {

  @Value("${spring.application.name:FFA Platform}")
  private String applicationName;

  @Value("${server.port:8080}")
  private String serverPort;

  /**
   * Get API information
   */
  @GetMapping("/info")
  @Operation(summary = "Get API information", description = "Get general information about the FFA API")
  public BaseResponse<Map<String, Object>> getApiInfo() {
    Map<String, Object> info = new HashMap<>();
    info.put("applicationName", applicationName);
    info.put("version", "1.0.0");
    info.put("description", "African Future Farmers Platform API");
    info.put("serverPort", serverPort);
    info.put("timestamp", LocalDateTime.now());
    info.put("status", "running");

    return BaseResponse.success("API information retrieved successfully", info);
  }

  /**
   * Get API health status
   */
  @GetMapping("/health")
  @Operation(summary = "API health check", description = "Check the health status of the API")
  public BaseResponse<Map<String, Object>> getHealth() {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("timestamp", LocalDateTime.now());
    health.put("application", applicationName);
    health.put("version", "1.0.0");

    return BaseResponse.success("API is healthy", health);
  }

  /**
   * Get API endpoints
   */
  @GetMapping("/endpoints")
  @Operation(summary = "Get API endpoints", description = "Get list of available API endpoints")
  public BaseResponse<Map<String, Object>> getEndpoints() {
    Map<String, Object> endpoints = new HashMap<>();

    Map<String, String> adminEndpoints = new HashMap<>();
    adminEndpoints.put("persons", "/ffaAPI/admin/persons");
    adminEndpoints.put("countries", "/ffaAPI/admin/countries");
    adminEndpoints.put("embassies", "/ffaAPI/admin/embassies");
    adminEndpoints.put("projects", "/ffaAPI/admin/projects");
    adminEndpoints.put("applications", "/ffaAPI/admin/applications");
    adminEndpoints.put("cities", "/ffaAPI/admin/cities");
    adminEndpoints.put("roles", "/ffaAPI/admin/roles");

    Map<String, String> intervenerEndpoints = new HashMap<>();
    intervenerEndpoints.put("projects", "/ffaAPI/intervener/projects");
    intervenerEndpoints.put("applications", "/ffaAPI/intervener/applications");
    intervenerEndpoints.put("messages", "/ffaAPI/intervener/messages");
    intervenerEndpoints.put("alerts", "/ffaAPI/intervener/alerts");
    intervenerEndpoints.put("profile", "/ffaAPI/intervener/profile");

    Map<String, String> userEndpoints = new HashMap<>();
    userEndpoints.put("projects", "/ffaAPI/user/projects");
    userEndpoints.put("applications", "/ffaAPI/user/applications");
    userEndpoints.put("messages", "/ffaAPI/user/messages");
    userEndpoints.put("alerts", "/ffaAPI/user/alerts");
    userEndpoints.put("profile", "/ffaAPI/user/profile");
    userEndpoints.put("countries", "/ffaAPI/user/countries");

    Map<String, String> authEndpoints = new HashMap<>();
    authEndpoints.put("login", "/ffaAPI/auth/login");
    authEndpoints.put("register", "/ffaAPI/auth/register");
    authEndpoints.put("logout", "/ffaAPI/auth/logout");
    authEndpoints.put("refresh", "/ffaAPI/auth/refresh");
    authEndpoints.put("forgot-password", "/ffaAPI/auth/forgot-password");
    authEndpoints.put("reset-password", "/ffaAPI/auth/reset-password");
    authEndpoints.put("change-password", "/ffaAPI/auth/change-password");
    authEndpoints.put("me", "/ffaAPI/auth/me");

    Map<String, String> publicEndpoints = new HashMap<>();
    publicEndpoints.put("countries", "/ffaAPI/public/countries");
    publicEndpoints.put("embassies", "/ffaAPI/public/embassies");
    publicEndpoints.put("projects", "/ffaAPI/public/projects");
    publicEndpoints.put("cities", "/ffaAPI/public/cities");
    publicEndpoints.put("health", "/ffaAPI/public/health");
    publicEndpoints.put("version", "/ffaAPI/public/version");

    endpoints.put("admin", adminEndpoints);
    endpoints.put("intervener", intervenerEndpoints);
    endpoints.put("user", userEndpoints);
    endpoints.put("auth", authEndpoints);
    endpoints.put("public", publicEndpoints);

    return BaseResponse.success("API endpoints retrieved successfully", endpoints);
  }

  /**
   * Get API documentation
   */
  @GetMapping("/docs")
  @Operation(summary = "Get API documentation", description = "Get information about API documentation")
  public BaseResponse<Map<String, Object>> getApiDocs() {
    Map<String, Object> docs = new HashMap<>();
    docs.put("swagger-ui", "/swagger-ui.html");
    docs.put("openapi-json", "/v3/api-docs");
    docs.put("openapi-yaml", "/v3/api-docs.yaml");
    docs.put("description", "Interactive API documentation is available via Swagger UI");

    return BaseResponse.success("API documentation information retrieved successfully", docs);
  }
}
