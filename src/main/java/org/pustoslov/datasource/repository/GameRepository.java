package org.pustoslov.datasource.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.pustoslov.datasource.model.GameEntity;
import org.pustoslov.domain.model.GameStatus;
import org.pustoslov.domain.model.RatingStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {
  @Query("SELECT g FROM GameEntity g " + "WHERE (g.xPlayerId = :userId OR g.oPlayerId = :userId)")
  List<GameEntity> findGameByParticipant(@Param("userId") UUID userId);

  @Query(
      "SELECT g FROM GameEntity g WHERE "
          + "((g.xPlayerId = :userId OR g.oPlayerId = :userId) AND "
          + "g.status IN :statuses)")
  List<GameEntity> findGamesByParticipantWithSpecificStatuses(
      @Param("userId") UUID userId, @Param("statuses") List<GameStatus> statuses);

  default List<GameEntity> findFinishedGamesByParticipant(UUID userId) {
    return findGamesByParticipantWithSpecificStatuses(
        userId, List.of(GameStatus.WIN, GameStatus.DRAW));
  }

  @Query(
      value =
          """
    SELECT
        stats.player_id,
        CASE
            WHEN (stats.losses + stats.draws) = 0 THEN stats.wins * 1.0
            ELSE stats.wins * 1.0 / (stats.losses + stats.draws)
        END as win_ratio
    FROM (
        SELECT
            x_player_id as player_id,
            SUM(CASE WHEN status = 'WIN' AND current_turn = x_player_id THEN 1 ELSE 0 END) as wins,
            SUM(CASE WHEN status = 'WIN' AND current_turn != x_player_id THEN 1 ELSE 0 END) as losses,
            SUM(CASE WHEN status = 'DRAW' THEN 1 ELSE 0 END) as draws
        FROM game
        WHERE status IN ('WIN', 'DRAW')
        GROUP BY x_player_id

        UNION ALL

        SELECT
            o_player_id as player_id,
            SUM(CASE WHEN status = 'WIN' AND current_turn = o_player_id THEN 1 ELSE 0 END) as wins,
            SUM(CASE WHEN status = 'WIN' AND current_turn != o_player_id THEN 1 ELSE 0 END) as losses,
            SUM(CASE WHEN status = 'DRAW' THEN 1 ELSE 0 END) as draws
        FROM game
        WHERE status IN ('WIN', 'DRAW')
        GROUP BY o_player_id
    ) as stats
    WHERE stats.wins + stats.losses + stats.draws > 0
    ORDER BY win_ratio DESC
    LIMIT :limit
    """,
      nativeQuery = true)
  List<Object[]> findTopPlayersByWinRatioNative(@Param("limit") int limit);

  default List<RatingStats> findTopPlayersByWinRatio(int limit) {
    return findTopPlayersByWinRatioNative(limit).stream()
        .map(result -> new RatingStats((UUID) result[0], ((Number) result[1]).doubleValue()))
        .collect(Collectors.toList());
  }

  List<GameEntity> findByoPlayerIdIsNull();
}
