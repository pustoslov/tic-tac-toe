package org.pustoslov.domain.model;

import java.util.UUID;

public class Game {
  private UUID id;
  private Board board;
  private GameStatus status;
  private UUID currentTurn;
  private UUID winner;

  private UUID xPlayerId;
  private UUID oPlayerId;

  public Game() {
    this.board = new Board();
    this.status = GameStatus.WAITING;
  }

  public Game(UUID id,
              Board board,
              GameStatus status,
              UUID currentTurn,
              UUID winner,
              UUID xPlayerId,
              UUID oPlayerId) {
    this.id = id;
    this.board = board;
    this.status = status;
    this.currentTurn = currentTurn;
    this.winner = winner;
    this.xPlayerId = xPlayerId;
    this.oPlayerId = oPlayerId;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public GameStatus getStatus() {
    return status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public UUID getXPlayerId() {
    return xPlayerId;
  }

  public void setXPlayerId(UUID xPlayerId) {
    this.xPlayerId = xPlayerId;
  }

  public UUID getOPlayerId() {
    return oPlayerId;
  }

  public void setOPlayerId(UUID oPlayerId) {
    this.oPlayerId = oPlayerId;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public UUID getCurrentTurn() {
    return currentTurn;
  }

  public void setCurrentTurn(UUID currentTurn) {
    this.currentTurn = currentTurn;
  }

  public UUID getWinner() {
    return winner;
  }

  public void setWinner(UUID winner) {
    this.winner = winner;
  }

}


