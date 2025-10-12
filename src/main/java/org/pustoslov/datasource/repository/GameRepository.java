package org.pustoslov.datasource.repository;

import org.pustoslov.datasource.model.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {
  @Query("SELECT g FROM GameEntity g " +
          "WHERE (g.xPlayerId = :userId OR g.oPlayerId = :userId)")
  List<GameEntity> findGameByParticipant(@Param("userId") UUID userId);

  List<GameEntity> findByoPlayerIdIsNull();
}

