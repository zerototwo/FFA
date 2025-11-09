package com.isep.ffa.dto.response;

import com.isep.ffa.entity.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication token response payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication tokens returned after successful login/refresh")
public class AuthTokensResponse {

  @Schema(description = "JWT access token (use as Bearer token)", example = "eyJhbGciOiJIUzUxMiJ9...")
  private String accessToken;

  @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzUxMiJ9...")
  private String refreshToken;

  @Schema(description = "Token type, typically 'Bearer'", example = "Bearer")
  private String tokenType;

  @Schema(description = "Access token expiration in seconds", example = "86400")
  private long expiresIn;

  @Schema(description = "User profile attached to the issued token", nullable = true)
  private Person user;
}

