package com.isep.ffa.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security Utility Class
 * Provides utility methods for security operations
 */
public class SecurityUtils {

  /**
   * Get current authenticated user
   */
  public static CustomUserDetailsService.CustomUserPrincipal getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null
        && authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal) {
      return (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
    }

    return null;
  }

  /**
   * Get current user ID
   */
  public static Long getCurrentUserId() {
    CustomUserDetailsService.CustomUserPrincipal user = getCurrentUser();
    return user != null ? user.getPersonId() : null;
  }

  /**
   * Get current user role
   */
  public static String getCurrentUserRole() {
    CustomUserDetailsService.CustomUserPrincipal user = getCurrentUser();
    return user != null ? user.getRole() : null;
  }

  /**
   * Check if current user has specific role
   */
  public static boolean hasRole(String role) {
    String currentRole = getCurrentUserRole();
    return currentRole != null && currentRole.equalsIgnoreCase(role);
  }

  /**
   * Check if current user is admin
   */
  public static boolean isAdmin() {
    return hasRole("ADMIN");
  }

  /**
   * Check if current user is intervener
   */
  public static boolean isIntervener() {
    return hasRole("INTERVENER");
  }

  /**
   * Check if current user is regular user
   */
  public static boolean isUser() {
    return hasRole("USER");
  }

  /**
   * Check if user is authenticated
   */
  public static boolean isAuthenticated() {
    return getCurrentUser() != null;
  }
}
