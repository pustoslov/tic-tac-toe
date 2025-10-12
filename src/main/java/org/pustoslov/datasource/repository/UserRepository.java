package org.pustoslov.datasource.repository;

import org.pustoslov.datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByUsername(String username);
  boolean existsByUsername(String username);
}
