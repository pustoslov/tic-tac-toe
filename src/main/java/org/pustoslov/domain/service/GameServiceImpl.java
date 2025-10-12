package org.pustoslov.domain.service;

import org.pustoslov.datasource.mapper.GameDatasourceMapper;
import org.pustoslov.datasource.model.GameEntity;
import org.pustoslov.datasource.model.UserEntity;
import org.pustoslov.datasource.repository.GameRepository;
import org.pustoslov.datasource.repository.UserRepository;
import org.pustoslov.domain.exception.GameNotFoundException;
import org.pustoslov.domain.exception.IllegalMoveException;
import org.pustoslov.domain.exception.NoAccessException;
import org.pustoslov.domain.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService {
  private final GameRepository gameRepository;
  private final UserRepository userRepository;
  private final GameDatasourceMapper gameDatasourceMapper;
  private final TicTacToeEngine ticTacToeEngine;

  public GameServiceImpl(GameRepository gameRepository,
                         UserRepository userRepository,
                         GameDatasourceMapper gameDatasourceMapper,
                         TicTacToeEngine ticTacToeEngine) {
    this.gameRepository = gameRepository;
    this.userRepository = userRepository;
    this.gameDatasourceMapper = gameDatasourceMapper;
    this.ticTacToeEngine = ticTacToeEngine;
  }

  @Override
  public Game createGame(UUID userId, GameMode mode) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    Game game = new Game();
    game.setXPlayerId(user.getId());
    game.setStatus(GameMode.PVE.equals(mode) ? GameStatus.TURN : GameStatus.WAITING);
    game.setCurrentTurn(userId);

    if (GameMode.PVE.equals(mode)) {
      game.setOPlayerId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
      game.setCurrentTurn(user.getId());
    }

    GameEntity entity = gameRepository.save(gameDatasourceMapper.toDataEntity(game));
    return gameDatasourceMapper.toDomain(entity);
  }

  @Override
  public Game findGameById(UUID gameId) {
    return gameDatasourceMapper.toDomain(gameRepository.findById(gameId)
            .orElseThrow(() -> new GameNotFoundException("There is no game with id: "
                    + gameId)));
  }

  @Override
  public void makeMove(UUID gameId, UUID userId, int row, int col) {
    Game game = findGameById(gameId);
    validateMove(row, col, userId, game);

    Board board = game.getBoard();

    int mark = userId.equals(game.getXPlayerId()) ? 1 : -1;
    board.setCell(row, col, mark);

    int winner = ticTacToeEngine.checkWinner(game.getBoard());

    if (winner != 0) {
      if (winner == 1) game.setWinner(game.getXPlayerId());
      else if (winner == -1) game.setWinner(game.getOPlayerId());
      game.setStatus(GameStatus.WIN);
    } else if (ticTacToeEngine.isBoardFull(board)) {
      game.setStatus(GameStatus.DRAW);
    } else {
      UUID next = userId.equals(game.getXPlayerId()) ? game.getOPlayerId() : game.getXPlayerId();
      game.setCurrentTurn(next);

      if (game.getOPlayerId() != null
              && game.getOPlayerId().toString().startsWith("00000000")) {
        ticTacToeEngine.makeMove(game);
        int winnerAfterBot = ticTacToeEngine.checkWinner(game.getBoard());
        if (winnerAfterBot != 0) {
          if (winnerAfterBot == 1) game.setWinner(userId);
          else if (winnerAfterBot == -1) game.setWinner(game.getOPlayerId());
          game.setStatus(GameStatus.WIN);
        } else if (ticTacToeEngine.isBoardFull(board)) {
          game.setStatus(GameStatus.DRAW);
        } else {
          game.setCurrentTurn(userId);
        }
      }

    }

    gameRepository.save(gameDatasourceMapper.toDataEntity(game));
  }

  @Override
  public List<Game> findAvailableGames(UUID userId) {
    return gameRepository.findByoPlayerIdIsNull().stream()
            .filter(entity -> !entity.getxPlayerId().equals(userId))
            .map(gameDatasourceMapper::toDomain)
            .collect(Collectors.toList());
  }

  @Override
  public List<Game> findOngoingGames(UUID userId) {
    List<GameEntity> entities = gameRepository.findGameByParticipant(userId);
    return entities.stream()
            .filter(entity -> entity.getStatus() != GameStatus.WIN && entity.getStatus()
                    != GameStatus.DRAW)
            .map(gameDatasourceMapper::toDomain)
            .toList();
  }

  @Override
  public Game joinGame(UUID gameId, UUID userId) {
    Game game = findGameById(gameId);
    if (game.getOPlayerId() != null) {
      throw new IllegalStateException("Game already has 2 players");
    }
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    game.setOPlayerId(user.getId());
    game.setStatus(GameStatus.TURN);
    game.setCurrentTurn(game.getXPlayerId());
    gameRepository.save(gameDatasourceMapper.toDataEntity(game));
    return game;
  }

  public void validateMove(int row, int col, UUID userId, Game game) {
    if (game.getOPlayerId() == null)
      throw new IllegalMoveException("Game not started yet.");
    if (!game.getOPlayerId().equals(userId) && !game.getXPlayerId().equals(userId))
      throw new NoAccessException("You have no access to this game");
    if (isGameOver(game))
      throw new IllegalMoveException("Game is over.");
    if (!game.getCurrentTurn().equals(userId))
      throw new IllegalMoveException("Not your turn");
    if (game.getBoard().getCell(row, col) != 0)
      throw new IllegalMoveException("You can't override cell.");
  }

  public boolean isGameOver(Game game) {
    if (game.getStatus() == GameStatus.DRAW ||
    game.getStatus() == GameStatus.WIN) return true;
    Board board = game.getBoard();
    int winner = ticTacToeEngine.checkWinner(board);
    if (winner != 0) return true;
    return ticTacToeEngine.isBoardFull(board);
  }

}
