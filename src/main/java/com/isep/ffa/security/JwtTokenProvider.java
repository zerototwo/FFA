package com.isep.ffa.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT Token Provider
 * Handles JWT token generation, validation, and extraction
 */
@Component
public class JwtTokenProvider {

  @Value("${app.jwt.secret:ffa-platform-super-secret-key-123456789012345678901234}")
  private String jwtSecret;

  @Value("${app.jwt.expiration:86400000}")
  private int jwtExpirationInMs;

  @Value("${app.jwt.refresh-expiration:604800000}")
  private int jwtRefreshExpirationInMs;

  /**
   * Generate JWT token from authentication
   */
  public String generateToken(Authentication authentication) {
    UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Generate JWT token from username
   */
  public String generateTokenFromUsername(String username) {
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Generate refresh token
   */
  public String generateRefreshToken(String username) {
    Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationInMs);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(getSigningKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Get username from JWT token
   */
  public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  /**
   * Validate JWT token
   */
  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      // Invalid JWT token
    } catch (ExpiredJwtException ex) {
      // Expired JWT token
    } catch (UnsupportedJwtException ex) {
      // Unsupported JWT token
    } catch (IllegalArgumentException ex) {
      // JWT claims string is empty
    }
    return false;
  }

  /**
   * Get signing key
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Get token expiration date
   */
  public Date getExpirationDateFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getExpiration();
  }

  /**
   * Check if token is expired
   */
  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * Get token type (access or refresh)
   */
  public String getTokenType(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.get("type", String.class);
  }
}
