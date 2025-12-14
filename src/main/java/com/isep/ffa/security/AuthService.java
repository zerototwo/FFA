package com.isep.ffa.security;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.request.RegisterRequest;
import com.isep.ffa.dto.response.AuthTokensResponse;
import com.isep.ffa.entity.Person;
import com.isep.ffa.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Authentication Service
 * Handles authentication and authorization logic
 */
@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private PersonService personService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Authenticate user with login credentials
   */
  public BaseResponse<AuthTokensResponse> authenticateUser(String login, String password) {
    try {
      // Validate input
      if (login == null || login.trim().isEmpty()) {
        return BaseResponse.error("Login is required", 400);
      }
      if (password == null || password.isEmpty()) {
        return BaseResponse.error("Password is required", 400);
      }

      // Check if user exists first (for better error messages)
      Optional<Person> personOpt = personService.findByLoginOrEmail(login.trim());
      if (personOpt.isEmpty()) {
        return BaseResponse.error("Invalid login credentials: User not found", 401);
      }

      Person person = personOpt.get();

      // Check if account is deleted/disabled
      if (person.getIsDeleted() != null && person.getIsDeleted()) {
        return BaseResponse.error("Account is disabled", 403);
      }

      // Check if password is set
      if (person.getPassword() == null || person.getPassword().trim().isEmpty()) {
        return BaseResponse.error("Account password is not set. Please reset your password.", 401);
      }

      // Check if password is in BCrypt format (starts with $2a$, $2b$, or $2y$)
      String storedPassword = person.getPassword();
      if (!storedPassword.startsWith("$2a$") && !storedPassword.startsWith("$2b$")
          && !storedPassword.startsWith("$2y$")) {
        return BaseResponse.error(
            "Password format is invalid. Please reset your password through registration or password reset.", 401);
      }

      // Debug: Log password format info (without exposing actual password)
      System.out.println("DEBUG: Attempting authentication for user: " + login.trim());
      System.out.println("DEBUG: Stored password format: "
          + storedPassword.substring(0, Math.min(10, storedPassword.length())) + "...");
      System.out.println("DEBUG: Input password length: " + password.length());

      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(login.trim(), password));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Generate JWT token
      String jwt = tokenProvider.generateToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(login);

      // Get user details
      CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
          .getPrincipal();
      Person authenticatedPerson = userPrincipal.getPerson();

      AuthTokensResponse response = AuthTokensResponse.builder()
          .accessToken(jwt)
          .refreshToken(refreshToken)
          .tokenType("Bearer")
          .expiresIn(604800) // 7 days in seconds (updated to match new expiration)
          .user(authenticatedPerson)
          .build();

      return BaseResponse.success("Login successful", response);

    } catch (BadCredentialsException e) {
      // Password mismatch - log for debugging
      System.err.println("=== BadCredentialsException ===");
      System.err.println("Error: " + e.getMessage());
      System.err.println("Login: " + (login != null ? login.trim() : "null"));
      System.err.println("Possible causes:");
      System.err.println("  1. Password does not match the stored BCrypt hash");
      System.err.println("  2. Password was encoded with a different BCrypt strength");
      System.err.println("  3. Password contains special characters that were not properly encoded");
      System.err.println("=================================");

      // Try to provide helpful suggestions and manual password test
      Optional<Person> personOpt = personService.findByLoginOrEmail(login != null ? login.trim() : "");
      if (personOpt.isPresent()) {
        Person person = personOpt.get();
        String storedPwd = person.getPassword();
        if (storedPwd != null) {
          System.err.println("Stored password format: " +
              (storedPwd.length() > 10 ? storedPwd.substring(0, 10) + "..." : storedPwd) +
              " (length: " + storedPwd.length() + ")");

          // Print full encrypted password for debugging
          System.err.println("=== Full BCrypt Hash (for debugging) ===");
          System.err.println("Encrypted password: " + storedPwd);
          System.err.println("=========================================");

          // Generate and print encrypted password for the input password
          try {
            String encryptedInputPassword = passwordEncoder.encode(password);
            System.err.println();
            System.err.println("=== Encrypted Password for Input Password ===");
            System.err.println("Input password: " + password);
            System.err.println("Encrypted (BCrypt): " + encryptedInputPassword);
            System.err.println("==============================================");
            System.err.println();
            System.err.println("=== SQL Update Statement (Copy and Execute) ===");
            System.err.println("UPDATE person SET password = '" + encryptedInputPassword +
                "' WHERE email = '" + (login != null ? login.trim() : "") + "';");
            System.err.println("================================================");
            System.err.println();
          } catch (Exception ex) {
            System.err.println("Error generating encrypted password: " + ex.getMessage());
          }

          // Manual password matching test
          try {
            boolean matches = passwordEncoder.matches(password, storedPwd);
            System.err.println("Manual password match test: " + (matches ? "MATCH" : "NO MATCH"));
            if (!matches) {
              System.err.println("The password you entered does not match the stored BCrypt hash.");
            }
          } catch (Exception ex) {
            System.err.println("Error during manual password test: " + ex.getMessage());
          }
        }
      } else {
        // Even if user not found, generate encrypted password for reference
        try {
          String encryptedInputPassword = passwordEncoder.encode(password);
          System.err.println();
          System.err.println("=== Encrypted Password for Input Password ===");
          System.err.println("Input password: " + password);
          System.err.println("Encrypted (BCrypt): " + encryptedInputPassword);
          System.err.println("==============================================");
        } catch (Exception ex) {
          System.err.println("Error generating encrypted password: " + ex.getMessage());
        }
      }

      return BaseResponse
          .error(
              "Invalid login credentials: Incorrect password. The password does not match the stored hash. Please re-register or reset your password.",
              401);
    } catch (UsernameNotFoundException e) {
      // User not found - log for debugging
      System.err.println("UsernameNotFoundException: " + e.getMessage());
      return BaseResponse.error("Invalid login credentials: User not found", 401);
    } catch (DisabledException e) {
      // Account disabled
      System.err.println("DisabledException: " + e.getMessage());
      return BaseResponse.error("Account is disabled", 403);
    } catch (LockedException e) {
      // Account locked
      System.err.println("LockedException: " + e.getMessage());
      return BaseResponse.error("Account is locked", 403);
    } catch (AuthenticationException e) {
      // Other authentication exceptions
      System.err.println("AuthenticationException: " + e.getClass().getName() + " - " + e.getMessage());
      e.printStackTrace();
      return BaseResponse.error("Authentication failed: " + e.getMessage(), 401);
    } catch (Exception e) {
      // Log the full exception for debugging
      System.err.println("Unexpected error during authentication: " + e.getClass().getName() + " - " + e.getMessage());
      e.printStackTrace();
      return BaseResponse.error("Authentication failed: " + e.getMessage(), 401);
    }
  }

  /**
   * Register new user
   */
  public BaseResponse<Person> registerUser(RegisterRequest request) {
    Person person = new Person();
    person.setFirstName(request.getFirstName());
    person.setLastName(request.getLastName());
    person.setEmail(request.getEmail());
    person.setLogin(request.getLogin());
    person.setPassword(request.getPassword());
    person.setAddress(request.getAddress());
    person.setCityId(request.getCityId());
    if (request.getOrganizationType() != null) {
      person.setOrganizationType(request.getOrganizationType().trim().toUpperCase());
    }
    person.setOrganizationId(request.getOrganizationId());
    person.setOrganizationName(request.getOrganizationName());

    return personService.createPerson(person);
  }

  /**
   * Refresh JWT token
   */
  public BaseResponse<AuthTokensResponse> refreshToken(String refreshToken) {
    try {
      if (tokenProvider.validateToken(refreshToken)) {
        String username = tokenProvider.getUsernameFromJWT(refreshToken);

        // Generate new access token
        String newAccessToken = tokenProvider.generateTokenFromUsername(username);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        Optional<Person> person = personService.findByLoginOrEmail(username);

        AuthTokensResponse response = AuthTokensResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(604800) // 7 days in seconds (updated to match new expiration)
            .user(person.orElse(null))
            .build();

        return BaseResponse.success("Token refreshed successfully", response);
      } else {
        return BaseResponse.error("Invalid refresh token: Refresh token is invalid or expired", 401);
      }
    } catch (Exception e) {
      return BaseResponse.error("Token refresh failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Get current authenticated user
   */
  public BaseResponse<Person> getCurrentUser() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null
          && authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal) {
        CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
            .getPrincipal();
        return BaseResponse.success("Current user retrieved", userPrincipal.getPerson());
      } else {
        return BaseResponse.error("No authenticated user: User not authenticated", 401);
      }
    } catch (Exception e) {
      return BaseResponse.error("Failed to get current user: " + e.getMessage(), 500);
    }
  }

  /**
   * Logout user
   */
  public BaseResponse<Boolean> logoutUser() {
    try {
      SecurityContextHolder.clearContext();
      return BaseResponse.success("Logout successful", true);
    } catch (Exception e) {
      return BaseResponse.error("Logout failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Change password for current authenticated user
   */
  public BaseResponse<Boolean> changePassword(String currentPassword, String newPassword) {
    try {
      // Get current user
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null
          || !(authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal)) {
        return BaseResponse.error("User not authenticated", 401);
      }

      CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
          .getPrincipal();
      Person person = userPrincipal.getPerson();

      // Verify current password
      try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(person.getLogin(), currentPassword));
      } catch (Exception e) {
        return BaseResponse.error("Current password is incorrect", 401);
      }

      // Update password
      person.setPassword(newPassword);
      BaseResponse<Person> updateResponse = personService.updatePerson(person);
      if (!updateResponse.isSuccess()) {
        return BaseResponse.error("Failed to update password: " + updateResponse.getMessage(), 500);
      }

      return BaseResponse.success("Password changed successfully", true);
    } catch (Exception e) {
      return BaseResponse.error("Password change failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Request password reset (forgot password)
   * Note: In a production environment, this should send an email with a reset
   * token.
   * For now, we'll generate a token and return it (in production, send via
   * email).
   */
  public BaseResponse<Boolean> forgotPassword(String email) {
    try {
      if (email == null || email.trim().isEmpty()) {
        return BaseResponse.error("Email is required", 400);
      }

      BaseResponse<Person> personResponse = personService.findByEmail(email.trim());
      if (!personResponse.isSuccess() || personResponse.getData() == null) {
        // Don't reveal if email exists or not (security best practice)
        return BaseResponse.success(
            "If the email exists, a password reset link has been sent", true);
      }

      // In production, generate a reset token and send it via email
      // For now, we'll just return success
      // TODO: Implement email sending and token storage
      return BaseResponse.success(
          "If the email exists, a password reset link has been sent", true);
    } catch (Exception e) {
      return BaseResponse.error("Password reset request failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Reset password with token
   * Note: In a production environment, this should validate the token from email.
   * For now, we'll use a simple token validation.
   */
  public BaseResponse<Boolean> resetPassword(String token, String newPassword) {
    try {
      if (token == null || token.trim().isEmpty()) {
        return BaseResponse.error("Reset token is required", 400);
      }
      if (newPassword == null || newPassword.trim().isEmpty()) {
        return BaseResponse.error("New password is required", 400);
      }

      // In production, validate token from database/cache and get user email
      // For now, we'll use JWT token validation as a simple approach
      // TODO: Implement proper token storage and validation
      if (!tokenProvider.validateToken(token)) {
        return BaseResponse.error("Invalid or expired reset token", 401);
      }

      String email = tokenProvider.getUsernameFromJWT(token);
      BaseResponse<Person> personResponse = personService.findByEmail(email);
      if (!personResponse.isSuccess() || personResponse.getData() == null) {
        return BaseResponse.error("User not found", 404);
      }

      Person person = personResponse.getData();
      person.setPassword(newPassword);
      BaseResponse<Person> updateResponse = personService.updatePerson(person);
      if (!updateResponse.isSuccess()) {
        return BaseResponse.error("Failed to reset password: " + updateResponse.getMessage(), 500);
      }

      return BaseResponse.success("Password reset successfully", true);
    } catch (Exception e) {
      return BaseResponse.error("Password reset failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Verify email address
   * Note: In a production environment, this should validate a verification token.
   */
  public BaseResponse<Boolean> verifyEmail(String token) {
    try {
      if (token == null || token.trim().isEmpty()) {
        return BaseResponse.error("Verification token is required", 400);
      }

      // In production, validate token from database and mark email as verified
      // For now, we'll use JWT token validation as a simple approach
      // TODO: Implement proper email verification token storage and validation
      if (!tokenProvider.validateToken(token)) {
        return BaseResponse.error("Invalid or expired verification token", 401);
      }

      String email = tokenProvider.getUsernameFromJWT(token);
      BaseResponse<Person> personResponse = personService.findByEmail(email);
      if (!personResponse.isSuccess() || personResponse.getData() == null) {
        return BaseResponse.error("User not found", 404);
      }

      // In production, update email_verified field in database
      // For now, just return success
      return BaseResponse.success("Email verified successfully", true);
    } catch (Exception e) {
      return BaseResponse.error("Email verification failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Resend verification email
   * Note: In a production environment, this should send a verification email.
   */
  public BaseResponse<Boolean> resendVerificationEmail(String email) {
    try {
      if (email == null || email.trim().isEmpty()) {
        return BaseResponse.error("Email is required", 400);
      }

      BaseResponse<Person> personResponse = personService.findByEmail(email.trim());
      if (!personResponse.isSuccess() || personResponse.getData() == null) {
        // Don't reveal if email exists or not (security best practice)
        return BaseResponse.success(
            "If the email exists, a verification email has been sent", true);
      }

      // In production, generate a verification token and send it via email
      // For now, we'll just return success
      // TODO: Implement email sending
      return BaseResponse.success(
          "If the email exists, a verification email has been sent", true);
    } catch (Exception e) {
      return BaseResponse.error("Failed to resend verification email: " + e.getMessage(), 500);
    }
  }
}
