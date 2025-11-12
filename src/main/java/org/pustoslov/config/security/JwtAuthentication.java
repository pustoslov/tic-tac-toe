package org.pustoslov.config.security;

import java.util.Collection;
import java.util.UUID;
import org.pustoslov.domain.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication implements Authentication {

  private final UUID uuid;
  private final Collection<Role> authorities;
  private boolean authenticated;

  public JwtAuthentication(UUID uuid, Collection<Role> authorities) {
    this.uuid = uuid;
    this.authorities = authorities;
    this.authenticated = true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return uuid;
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
    this.authenticated = authenticated;
  }

  @Override
  public String getName() {
    return uuid != null ? uuid.toString() : null;
  }
}
