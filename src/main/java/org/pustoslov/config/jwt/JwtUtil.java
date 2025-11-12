package org.pustoslov.config.jwt;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.pustoslov.config.security.JwtAuthentication;
import org.pustoslov.domain.model.Role;
import org.pustoslov.domain.model.RoleName;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  public JwtAuthentication generateAuth(Claims claims) {
    UUID uuid = getUuidFromClaims(claims);
    List<Role> authorities = extractAuthorities(claims);
    return new JwtAuthentication(uuid, authorities);
  }

  @SuppressWarnings("unchecked")
  private List<Role> extractAuthorities(Claims claims) {
    List<Role> authorities = new ArrayList<>();

    List<String> roleNames = claims.get("roles", List.class);
    if (roleNames != null) {
      for (String roleName : roleNames) {
        try {
          RoleName roleNameEnum = RoleName.valueOf(roleName);
          Role role = new Role(roleNameEnum);
          authorities.add(role);
        } catch (IllegalArgumentException e) {
          System.err.println("Unknown role in JWT token: " + roleName);
        }
      }
    }

    return authorities;
  }

  public UUID getUuidFromClaims(Claims claims) {
    String uuidStr = claims.get("uuid", String.class);
    return UUID.fromString(uuidStr);
  }
}
