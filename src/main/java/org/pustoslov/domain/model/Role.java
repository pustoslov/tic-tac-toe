package org.pustoslov.domain.model;

import org.springframework.security.core.GrantedAuthority;

public record Role(RoleName roleName) implements GrantedAuthority {

  public RoleName getName() {
    return roleName;
  }

  @Override
  public String getAuthority() {
    return roleName.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return role.roleName.equals(roleName);
  }
}
