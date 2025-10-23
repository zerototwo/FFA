package com.isep.ffa.security;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.entity.Person;
import com.isep.ffa.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication Service
 * Handles authentication and authorization logic
 */
// @Service
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
  public BaseResponse<Map<String, Object>> authenticateUser(String login, String password) {
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

      // Prepare response
      Map<String, Object> response = new HashMap<>();
      response.put("accessToken", jwt);
      response.put("refreshToken", refreshToken);
      response.put("tokenType", "Bearer");
      response.put("expiresIn", 86400); // 24 hours
      response.put("user", person);
      response.put("role", userPrincipal.getRole());

      return BaseResponse.success("Login successful", response);

    } catch (Exception e) {
      return BaseResponse.error("Authentication failed: " + e.getMessage(), 401);
    }
  }

  /**
   * Register new user
   */
  public BaseResponse<Person> registerUser(Person person) {
    try {
      // Check if user already exists
      Optional<Person> existingUser = personService.findByLoginOrEmail(person.getLogin());
      if (existingUser.isPresent()) {
        return BaseResponse.error("User already exists: Login or email already registered", 400);
      }

      // Encode password
      String encodedPassword = passwordEncoder.encode("defaultPassword"); // TODO: Get from request
      // person.setPassword(encodedPassword); // TODO: Add password field to Person
      // entity

      // Set default role (USER)
      // TODO: Set default role

      // Save user
      BaseResponse<Person> result = personService.createPerson(person);

      if (result.isSuccess()) {
        return BaseResponse.success("User registered successfully", result.getData());
      } else {
        return result;
      }

    } catch (Exception e) {
      return BaseResponse.error("Registration failed: " + e.getMessage(), 500);
    }
  }

  /**
   * Refresh JWT token
   */
  public BaseResponse<Map<String, Object>> refreshToken(String refreshToken) {
    try {
      if (tokenProvider.validateToken(refreshToken)) {
        String username = tokenProvider.getUsernameFromJWT(refreshToken);

        // Generate new access token
        String newAccessToken = tokenProvider.generateTokenFromUsername(username);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", 86400);

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
}
