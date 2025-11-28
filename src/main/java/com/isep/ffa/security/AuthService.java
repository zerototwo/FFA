package com.isep.ffa.security;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.request.RegisterRequest;
import com.isep.ffa.dto.response.AuthTokensResponse;
import com.isep.ffa.entity.Person;
import com.isep.ffa.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  /**
   * Authenticate user with login credentials
   */
  public BaseResponse<AuthTokensResponse> authenticateUser(String login, String password) {
    try {
      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(login, password));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Generate JWT token
      String jwt = tokenProvider.generateToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(login);

      // Get user details
      CustomUserDetailsService.CustomUserPrincipal userPrincipal = (CustomUserDetailsService.CustomUserPrincipal) authentication
          .getPrincipal();
      Person person = userPrincipal.getPerson();

      AuthTokensResponse response = AuthTokensResponse.builder()
          .accessToken(jwt)
          .refreshToken(refreshToken)
          .tokenType("Bearer")
          .expiresIn(604800) // 7 days in seconds (updated to match new expiration)
          .user(person)
          .build();

      return BaseResponse.success("Login successful", response);

    } catch (Exception e) {
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
