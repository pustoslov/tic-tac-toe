package org.pustoslov.domain.service;

import org.pustoslov.domain.model.Board;
import org.pustoslov.domain.model.Game;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeEngine {
  private final int X = 1;
  private final int O = -1;
  private final int EMPTY = 0;

  private int[] getBestMove(Board board) {
    int[] bestMove = new int[2];
    int bestValue = Integer.MIN_VALUE;

    for (int i = 0; i < Board.SIZE; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        if (board.getCell(i, j) == EMPTY) {
          board.setCell(i, j, O);
          int moveValue = minimax(board, false, O);
          board.setCell(i, j, EMPTY);

          if (moveValue > bestValue) {
            bestMove[0] = i;
            bestMove[1] = j;
            bestValue = moveValue;
          }
        }
      }
    }

    return bestMove;
  }

  private int minimax(Board board, boolean isMaximizing, int player) {
    int winner = checkWinner(board);
    if (winner != 0) {
      return winner == player ? 10 : -10;
    }
    if (isBoardFull(board)) {
      return 0;
    }

    int bestValue;
    if (isMaximizing) {
      bestValue = Integer.MIN_VALUE;

      for (int i = 0; i < Board.SIZE; i++) {
        for (int j = 0; j < Board.SIZE; j++) {
          if (board.getCell(i, j) == EMPTY) {
            board.setCell(i, j, player);
            int value = minimax(board, false, player);
            board.setCell(i, j, EMPTY);
            bestValue = Math.max(bestValue, value);
          }
        }
      }

    } else {
      bestValue = Integer.MAX_VALUE;

      for (int i = 0; i < Board.SIZE; i++) {
        for (int j = 0; j < Board.SIZE; j++) {
          if (board.getCell(i, j) == EMPTY) {
            board.setCell(i, j, player == X ? O : X);
            int value = minimax(board, true, player);
            board.setCell(i, j, EMPTY);
            bestValue = Math.min(bestValue, value);
          }
        }
      }

    }
    return bestValue;
  }

  public int checkWinner(Board board) {
    for (int i = 0; i < Board.SIZE; i++) {
      if (board.getCell(i, 0) != EMPTY &&
              board.getCell(i, 0) == board.getCell(i, 1) &&
              board.getCell(i, 1) == board.getCell(i, 2)) {
        return board.getCell(i, 0);
      }
      if (board.getCell(0, i) != EMPTY &&
              board.getCell(0, i) == board.getCell(1, i) &&
              board.getCell(1, i) == board.getCell(2, i)) {
        return board.getCell(0, i);
      }
    }

    if (board.getCell(0, 0) != EMPTY &&
            board.getCell(0, 0) == board.getCell(1, 1) &&
            board.getCell(1, 1) == board.getCell(2, 2)) {
      return board.getCell(0, 0);
    }

    if (board.getCell(0, 2) != EMPTY &&
            board.getCell(0, 2) == board.getCell(1, 1) &&
            board.getCell(1, 1) == board.getCell(2, 0)) {
      return board.getCell(0, 2);
    }

    return 0;
  }

  public boolean isBoardFull(Board board) {
    for (int i = 0; i < Board.SIZE; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        if (board.getCell(i, j) == EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  public void makeMove(Game game) {
    int[] move = getBestMove(game.getBoard());
    game.getBoard().setCell(move[0], move[1], O);
  }


}
