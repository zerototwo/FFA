package com.isep.ffa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login request payload
 */
@Data
@Schema(description = "Payload for authenticating an existing user")
public class LoginRequest {

  @Schema(description = "Login identifier (username or email)", example = "alice.johnson@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Login is required")
  private String login;

  @Schema(description = "User password", example = "Secret123!", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Password is required")
  private String password;
}

