package org.pustoslov.domain.service;

import java.util.List;
import java.util.UUID;
import org.pustoslov.domain.model.Game;
import org.pustoslov.domain.model.GameMode;

public interface GameService {
  Game createGame(UUID userId, GameMode mode);

  void makeMove(UUID gameId, UUID userId, int row, int col);

  List<Game> findAvailableGames(UUID userId);

  List<Game> findOngoingGames(UUID userId);

  Game joinGame(UUID gameId, UUID userId);

  Game findGameById(UUID gameId);
}
