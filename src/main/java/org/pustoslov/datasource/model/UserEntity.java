package org.pustoslov.datasource.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import org.pustoslov.domain.model.RoleName;

@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "user_name", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_name", referencedColumnName = "user_name"))
  @Column(name = "role_name", length = 50)
  @Enumerated(EnumType.STRING)
  private Set<RoleName> roleNames;

  public UserEntity() {}

  public UserEntity(String username, String password, Set<RoleName> roleNames) {
    this.username = username;
    this.password = password;
    this.roleNames = roleNames != null ? new HashSet<>(roleNames) : new HashSet<>();
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

  public Set<RoleName> getRoleNames() {
    return roleNames;
  }
}
