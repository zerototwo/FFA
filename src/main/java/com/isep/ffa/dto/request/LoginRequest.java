package com.isep.ffa.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login request payload
 */
@Data
public class LoginRequest {

  @NotBlank(message = "Login is required")
  private String login;

  @NotBlank(message = "Password is required")
  private String password;
}

