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
          .expiresIn(86400)
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
            .expiresIn(86400)
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
}
