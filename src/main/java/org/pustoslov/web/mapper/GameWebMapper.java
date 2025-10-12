package org.pustoslov.web.mapper;

import org.pustoslov.domain.model.Game;
import org.pustoslov.web.model.GameResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GameWebMapper {

  public GameResponse toDTO(Game game) {
    int[][] matrix = game.getBoard().getMatrix();

    List<List<Integer>> boardList = Arrays.stream(matrix)
            .map(row -> Arrays.stream(row).boxed().toList())
            .toList();

    return new GameResponse(game.getId(), game.getXPlayerId(), game.getOPlayerId(),
            game.getCurrentTurn(), boardList);
  }
}


