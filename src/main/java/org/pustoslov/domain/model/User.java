package org.pustoslov.domain.model;

import java.util.*;

public class User {
  private UUID id;
  private String username;
  private String password;
  private final Set<Role> roles;

  public User(String username, String password, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = new HashSet<>(roles);
  }

  public Collection<Role> getAuthorities() {
    return roles;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean hasRole(RoleName roleName) {
    return roles.stream().anyMatch(role -> role.getAuthority().equals(roleName.toString()));
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }
}
