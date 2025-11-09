package com.isep.ffa.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Register request payload
 */
@Data
public class RegisterRequest {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Login is required")
  private String login;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  private String address;

  private Long cityId;

  /**
   * Organisation type selected in the form.
   * Expected values: EMBASSY / INSTITUTION / OTHER (optional)
   */
  private String organizationType;

  /**
   * Organisation identifier when the user selects an existing embassy/institution.
   */
  private Long organizationId;

  /**
   * Organisation name when the user selects "Other organisation".
   */
  private String organizationName;
}

