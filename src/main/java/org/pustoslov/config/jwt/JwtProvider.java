package org.pustoslov.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.pustoslov.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access.expiration}")
  private Long accessExpiration;

  @Value("${jwt.refresh.expiration}")
  private Long refreshExpiration;

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(User user) {
    List<String> roleNames =
        user.getAuthorities().stream()
            .map(role -> role.getName().toString())
            .collect(Collectors.toList());

    Instant now = Instant.now();
    Instant expiration = now.plusMillis(accessExpiration);

    return Jwts.builder()
        .subject(user.getUsername())
        .claim("uuid", user.getId().toString())
        .claim("roles", roleNames)
        .claim("type", "access")
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(getSigningKey())
        .compact();
  }

  public String generateRefreshToken(User user) {
    Instant now = Instant.now();
    Instant expiration = now.plusMillis(refreshExpiration);

    return Jwts.builder()
        .subject(user.getUsername())
        .claim("uuid", user.getId().toString())
        .claim("type", "refresh")
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateAccessToken(String token) {
    try {
      Claims claims = parseToken(token);
      return "access".equals(claims.get("type", String.class))
          && !isTokenExpired(claims)
          && claims.get("uuid", String.class) != null
          && claims.get("roles", List.class) != null;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean validateRefreshToken(String token) {
    try {
      Claims claims = parseToken(token);
      return "refresh".equals(claims.get("type", String.class))
          && !isTokenExpired(claims)
          && claims.get("uuid", String.class) != null;
    } catch (Exception e) {
      return false;
    }
  }

  public Claims parseToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public UUID getUuidFromRefreshToken(String refreshToken) {
    try {
      Claims claims = parseToken(refreshToken);
      String uuidString = claims.get("uuid", String.class);
      if (uuidString == null || uuidString.trim().isEmpty()) {
        throw new RuntimeException("UUID claim is missing in refresh token");
      }
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid UUID format in refresh token: " + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("Failed to extract UUID from refresh token: " + e.getMessage());
    }
  }

  private boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }
}
