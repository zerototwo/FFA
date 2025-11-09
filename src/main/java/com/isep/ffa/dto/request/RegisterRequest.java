package com.isep.ffa.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Register request payload
 */
@Data
@Schema(description = "Payload for registering a new user account")
public class RegisterRequest {

  @Schema(description = "User first name", example = "Alice", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "First name is required")
  private String firstName;

  @Schema(description = "User last name", example = "Johnson", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Last name is required")
  private String lastName;

  @Schema(description = "Unique email address", example = "alice.johnson@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(description = "Unique login username", example = "alice.johnson", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Login is required")
  private String login;

  @Schema(description = "Account password (minimum 6 characters)", example = "Secret123!", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @Schema(description = "Postal address", example = "123 Embassy Road, Ottawa")
  private String address;

  @Schema(description = "City identifier associated with the user", example = "6")
  private Long cityId;

  /**
   * Organisation type selected in the form.
   * Expected values: EMBASSY / INSTITUTION / OTHER (optional)
   */
  @Schema(description = "Organisation type selected during registration (EMBASSY / INSTITUTION / OTHER)", example = "EMBASSY")
  private String organizationType;

  /**
   * Organisation identifier when the user selects an existing embassy/institution.
   */
  @Schema(description = "Organisation identifier when selecting an existing embassy/institution", example = "3")
  private Long organizationId;

  /**
   * Organisation name when the user selects "Other organisation".
   */
  @Schema(description = "Organisation name when selecting \"Other organisation\"", example = "Alliance Française")
  private String organizationName;
}

