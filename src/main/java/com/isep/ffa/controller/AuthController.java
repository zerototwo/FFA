package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.request.RegisterRequest;
import com.isep.ffa.dto.request.LoginRequest;
import com.isep.ffa.dto.response.AuthTokensResponse;
import com.isep.ffa.entity.Person;
import com.isep.ffa.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Provides REST API endpoints for authentication operations
 * Base path: /ffaAPI/auth
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Authentication and authorization operations")
public class AuthController {

  @Autowired
  private AuthService authService;

  /**
   * User login
   */
  @PostMapping("/login")
  @Operation(summary = "User login", description = "Authenticate user with login credentials",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
          content = @Content(schema = @Schema(implementation = LoginRequest.class),
              examples = @ExampleObject(name = "LoginRequest",
                  value = "{\n"
                      + "  \"login\": \"alice.johnson@example.com\",\n"
                      + "  \"password\": \"Secret123!\"\n"
                      + "}"))))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login successful",
          content = @Content(schema = @Schema(implementation = AuthTokensResponse.class))),
      @ApiResponse(responseCode = "401", description = "Invalid credentials"),
      @ApiResponse(responseCode = "500", description = "Unexpected error")
  })
  public BaseResponse<AuthTokensResponse> login(@RequestBody @Valid LoginRequest request) {
    return authService.authenticateUser(request.getLogin(), request.getPassword());
  }

  /**
   * User registration
   */
  @PostMapping("/register")
  @Operation(summary = "User registration", description = "Register a new user account. "
      + "The API is served under the `/ffaAPI` context path (full URL: `/ffaAPI/auth/register`).",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
          content = @Content(schema = @Schema(implementation = RegisterRequest.class),
              examples = @ExampleObject(name = "RegisterRequest",
                  value = "{\n"
                      + "  \"firstName\": \"Alice\",\n"
                      + "  \"lastName\": \"Johnson\",\n"
                      + "  \"email\": \"alice.johnson@example.com\",\n"
                      + "  \"login\": \"alice.johnson\",\n"
                      + "  \"password\": \"Secret123!\",\n"
                      + "  \"address\": \"123 Embassy Road\",\n"
                      + "  \"cityId\": 6,\n"
                      + "  \"organizationType\": \"EMBASSY\",\n"
                      + "  \"organizationId\": 3,\n"
                      + "  \"organizationName\": null\n"
                      + "}"))))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Registration successful",
          content = @Content(schema = @Schema(implementation = Person.class))),
      @ApiResponse(responseCode = "400", description = "Validation error"),
      @ApiResponse(responseCode = "409", description = "Duplicate login/email"),
      @ApiResponse(responseCode = "500", description = "Unexpected error")
  })
  public BaseResponse<Person> register(@RequestBody @Valid RegisterRequest request) {
    return authService.registerUser(request);
  }

  /**
   * User logout
   */
  @PostMapping("/logout")
  @Operation(summary = "User logout", description = "Logout current user")
  public BaseResponse<Boolean> logout() {
    return authService.logoutUser();
  }

  /**
   * Refresh token
   */
  @PostMapping("/refresh")
  @Operation(summary = "Refresh token", description = "Refresh authentication token")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Token refreshed",
          content = @Content(schema = @Schema(implementation = AuthTokensResponse.class))),
      @ApiResponse(responseCode = "401", description = "Invalid refresh token"),
      @ApiResponse(responseCode = "500", description = "Unexpected error")
  })
  public BaseResponse<AuthTokensResponse> refreshToken(
      @Parameter(description = "Refresh token") @RequestParam String refreshToken) {
    return authService.refreshToken(refreshToken);
  }

  /**
   * Forgot password
   */
  @PostMapping("/forgot-password")
  @Operation(summary = "Forgot password", description = "Request password reset. In production, this sends an email with a reset link.")
  public BaseResponse<Boolean> forgotPassword(
      @Parameter(description = "Email address") @RequestParam String email) {
    return authService.forgotPassword(email);
  }

  /**
   * Reset password
   */
  @PostMapping("/reset-password")
  @Operation(summary = "Reset password", description = "Reset password with token from email")
  public BaseResponse<Boolean> resetPassword(
      @Parameter(description = "Reset token") @RequestParam String token,
      @Parameter(description = "New password") @RequestParam String newPassword) {
    return authService.resetPassword(token, newPassword);
  }

  /**
   * Change password
   */
  @PostMapping("/change-password")
  @Operation(summary = "Change password", description = "Change current authenticated user password")
  public BaseResponse<Boolean> changePassword(
      @Parameter(description = "Current password") @RequestParam String currentPassword,
      @Parameter(description = "New password") @RequestParam String newPassword) {
    return authService.changePassword(currentPassword, newPassword);
  }

  /**
   * Get current user info
   */
  @GetMapping("/me")
  @Operation(summary = "Get current user", description = "Get current authenticated user information")
  public BaseResponse<Person> getCurrentUser() {
    return authService.getCurrentUser();
  }

  /**
   * Verify email
   */
  @PostMapping("/verify-email")
  @Operation(summary = "Verify email", description = "Verify user email address with token from email")
  public BaseResponse<Boolean> verifyEmail(
      @Parameter(description = "Verification token") @RequestParam String token) {
    return authService.verifyEmail(token);
  }

  /**
   * Resend verification email
   */
  @PostMapping("/resend-verification")
  @Operation(summary = "Resend verification email", description = "Resend email verification link")
  public BaseResponse<Boolean> resendVerificationEmail(
      @Parameter(description = "Email address") @RequestParam String email) {
    return authService.resendVerificationEmail(email);
  }
}
