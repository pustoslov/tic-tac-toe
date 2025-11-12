package org.pustoslov.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Game {
  private UUID id;
  private final Board board;
  private GameStatus status;
  private UUID currentTurn;
  private UUID winner;
  private final Instant timestamp;

  private UUID xPlayerId;
  private UUID oPlayerId;

  public Game() {
    this.board = new Board();
    this.status = GameStatus.WAITING;
    this.timestamp = Instant.now();
  }

  public Game(
      UUID id,
      Board board,
      GameStatus status,
      UUID currentTurn,
      UUID winner,
      UUID xPlayerId,
      UUID oPlayerId,
      Instant timestamp) {
    this.id = id;
    this.board = board;
    this.status = status;
    this.currentTurn = currentTurn;
    this.winner = winner;
    this.xPlayerId = xPlayerId;
    this.oPlayerId = oPlayerId;
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
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

  public Instant getTimestamp() {
    return timestamp;
  }
}
