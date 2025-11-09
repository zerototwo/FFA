package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.request.LoginRequest;
import com.isep.ffa.entity.Person;
import com.isep.ffa.security.AuthService;
import com.isep.ffa.dto.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
  @Operation(summary = "User login", description = "Authenticate user with login credentials")
  public BaseResponse<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
    return authService.authenticateUser(request.getLogin(), request.getPassword());
  }

  /**
   * User registration
   */
  @PostMapping("/register")
  @Operation(summary = "User registration", description = "Register a new user")
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
  public BaseResponse<Map<String, Object>> refreshToken(
      @Parameter(description = "Refresh token") @RequestParam String refreshToken) {
    return authService.refreshToken(refreshToken);
  }

  /**
   * Forgot password
   */
  @PostMapping("/forgot-password")
  @Operation(summary = "Forgot password", description = "Request password reset")
  public BaseResponse<Boolean> forgotPassword(
      @Parameter(description = "Email address") @RequestParam String email) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Reset password
   */
  @PostMapping("/reset-password")
  @Operation(summary = "Reset password", description = "Reset password with token")
  public BaseResponse<Boolean> resetPassword(
      @Parameter(description = "Reset token") @RequestParam String token,
      @Parameter(description = "New password") @RequestParam String newPassword) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Change password
   */
  @PostMapping("/change-password")
  @Operation(summary = "Change password", description = "Change current user password")
  public BaseResponse<Boolean> changePassword(
      @Parameter(description = "Current password") @RequestParam String currentPassword,
      @Parameter(description = "New password") @RequestParam String newPassword) {
    // TODO: Implement business logic
    return null;
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
  @Operation(summary = "Verify email", description = "Verify user email address")
  public BaseResponse<Boolean> verifyEmail(
      @Parameter(description = "Verification token") @RequestParam String token) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Resend verification email
   */
  @PostMapping("/resend-verification")
  @Operation(summary = "Resend verification email", description = "Resend email verification")
  public BaseResponse<Boolean> resendVerificationEmail(
      @Parameter(description = "Email address") @RequestParam String email) {
    // TODO: Implement business logic
    return null;
  }
}
