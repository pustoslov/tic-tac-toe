package org.pustoslov.datasource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

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

  public UserEntity() {}

  public UserEntity(String username, String password) {
    this.username = username;
    this.password = password;
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
}
