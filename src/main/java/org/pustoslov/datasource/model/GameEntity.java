package org.pustoslov.datasource.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.pustoslov.datasource.converter.BoardConverter;
import org.pustoslov.domain.model.GameStatus;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game")
public class GameEntity{
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @Column(columnDefinition = "UUID", updatable = false, nullable = false)
  private UUID gameId;

  @Convert(converter = BoardConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<List<Integer>> board;

  @Column(nullable = false)
  private GameStatus status;

  @Column(nullable = false)
  private UUID currentTurn;

  private UUID winner;

  @Column(nullable = false)
  private UUID xPlayerId;

  private UUID oPlayerId;

  public GameEntity() {}

  public GameEntity(UUID id, List<List<Integer>> board,
                    GameStatus status,
                    UUID currentTurn,
                    UUID winner,
                    UUID xPlayerId,
                    UUID oPlayerId) {
    this.gameId = id;
    this.board = board;
    this.status = status;
    this.currentTurn = currentTurn;
    this.winner = winner;
    this.xPlayerId = xPlayerId;
    this.oPlayerId = oPlayerId;
  }

  public UUID getId() {
    return gameId;
  }

  public void setId(UUID uuid) {
    this.gameId = uuid;
  }

  public List<List<Integer>> getBoard() {
    return board;
  }

  public void setBoard(List<List<Integer>> board) {
    this.board = board;
  }

  public GameStatus getStatus() {
    return status;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
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

  public UUID getxPlayerId() {
    return xPlayerId;
  }

  public void setxPlayerId(UUID xPlayerId) {
    this.xPlayerId = xPlayerId;
  }

  public UUID getoPlayerId() {
    return oPlayerId;
  }

  public void setoPlayerId(UUID oPlayerId) {
    this.oPlayerId = oPlayerId;
  }
}
