package org.pustoslov.datasource.repository;

import java.util.Optional;
import java.util.UUID;
import org.pustoslov.datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);
}
