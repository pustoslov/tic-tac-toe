package org.pustoslov.datasource.mapper;

import java.util.Arrays;
import java.util.List;
import org.pustoslov.datasource.model.GameEntity;
import org.pustoslov.domain.model.Board;
import org.pustoslov.domain.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameDatasourceMapper {

  public GameEntity toDataEntity(Game game) {
    int[][] matrix = game.getBoard().getMatrix();

    List<List<Integer>> boardList =
        Arrays.stream(matrix).map(row -> Arrays.stream(row).boxed().toList()).toList();

    GameEntity entity;
    if (game.getId() != null) {
      entity =
          new GameEntity(
              game.getId(),
              boardList,
              game.getStatus(),
              game.getCurrentTurn(),
              game.getWinner(),
              game.getXPlayerId(),
              game.getOPlayerId());
    } else {
      entity = new GameEntity();
      entity.setBoard(boardList);
      entity.setStatus(game.getStatus());
      entity.setCurrentTurn(game.getCurrentTurn());
      entity.setWinner(game.getWinner());
      entity.setxPlayerId(game.getXPlayerId());
      entity.setoPlayerId(game.getOPlayerId());
    }

    return entity;
  }

  public GameEntity toDataEntity(Board board) {
    int[][] matrix = board.getMatrix();

    List<List<Integer>> boardList =
        Arrays.stream(matrix).map(row -> Arrays.stream(row).boxed().toList()).toList();

    GameEntity entity = new GameEntity();
    entity.setBoard(boardList);
    return entity;
  }

  public Game toDomain(GameEntity entity) {
    int[][] matrix =
        entity.getBoard().stream()
            .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
            .toArray(int[][]::new);

    return new Game(
        entity.getId(),
        new Board(matrix),
        entity.getStatus(),
        entity.getCurrentTurn(),
        entity.getWinner(),
        entity.getxPlayerId(),
        entity.getoPlayerId());
  }
}
