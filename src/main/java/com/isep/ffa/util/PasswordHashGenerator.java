package com.isep.ffa.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password Hash Generator Utility
 * Temporary utility class to generate BCrypt password hashes
 * 
 * Usage: Run the main method to generate a BCrypt hash for a password
 */
public class PasswordHashGenerator {

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * Generate BCrypt hash for a password
   * 
   * @param password Plain text password
   * @return BCrypt hash
   */
  public static String generateHash(String password) {
    return passwordEncoder.encode(password);
  }

  /**
   * Test password matching
   * 
   * @param rawPassword     Plain text password
   * @param encodedPassword BCrypt hash
   * @return true if password matches
   */
  public static boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  /**
   * Main method to generate hash for testing
   * Run this to generate a BCrypt hash for "Secret123!"
   */
  public static void main(String[] args) {
    String password = "Secret123!";
    String hash = generateHash(password);
    
    System.out.println("========================================");
    System.out.println("Password: " + password);
    System.out.println("BCrypt Hash: " + hash);
    System.out.println("========================================");
    System.out.println();
    System.out.println("SQL Update Statement:");
    System.out.println("UPDATE person SET password = '" + hash + "' WHERE email = 'alice.johnson@example.com';");
    System.out.println();
    System.out.println("Verification:");
    System.out.println("Password matches: " + matches(password, hash));
  }
}

