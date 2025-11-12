package org.pustoslov.web.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.pustoslov.domain.exception.IllegalRequestParameter;
import org.pustoslov.domain.model.Game;
import org.pustoslov.domain.model.GameMode;
import org.pustoslov.domain.service.GameService;
import org.pustoslov.web.mapper.GameWebMapper;
import org.pustoslov.web.model.requests.MoveRequest;
import org.pustoslov.web.model.responses.GameResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {

  private final GameService gameService;
  private final GameWebMapper gameMapper;

  public GameController(GameService gameService, GameWebMapper gameMapper) {
    this.gameService = gameService;
    this.gameMapper = gameMapper;
  }

  @PostMapping("/{gameId}/move")
  public void makeMove(
      @Valid @RequestBody MoveRequest request,
      @PathVariable UUID gameId,
      Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    gameService.makeMove(gameId, userId, request.row(), request.col());
  }

  @PostMapping("/new")
  public ResponseEntity<GameResponse> newGame(
      @RequestParam String mode, Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    GameMode gameMode;
    try {
      gameMode = GameMode.valueOf(mode.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalRequestParameter("'mode' must be 'pve' or 'pvp'");
    }
    Game newGame = gameService.createGame(userId, gameMode);
    return ResponseEntity.ok(gameMapper.toDTO(newGame));
  }

  @GetMapping("/ongoing")
  public ResponseEntity<List<UUID>> getOngoingGames(Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    return ResponseEntity.ok(
        gameService.findOngoingGames(userId).stream()
            .map(gameMapper::toDTO)
            .map(GameResponse::gameId)
            .collect(Collectors.toList()));
  }

  @GetMapping("/available")
  public ResponseEntity<List<UUID>> getAvailableGames(Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    return ResponseEntity.ok(
        gameService.findAvailableGames(userId).stream()
            .map(gameMapper::toDTO)
            .map(GameResponse::gameId)
            .collect(Collectors.toList()));
  }

  @PostMapping("/{gameId}/join")
  public ResponseEntity<GameResponse> joinGame(
      @PathVariable UUID gameId, Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    GameResponse response = gameMapper.toDTO(gameService.joinGame(gameId, userId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{gameId}")
  public ResponseEntity<GameResponse> getGame(@PathVariable UUID gameId) {
    GameResponse response = gameMapper.toDTO(gameService.findGameById(gameId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/finished")
  public ResponseEntity<List<GameResponse>> getFinishedGames(Authentication authentication) {
    UUID id = (UUID) authentication.getPrincipal();
    return ResponseEntity.ok(
        gameService.findFinishedGamesById(id).stream().map(gameMapper::toDTO).toList());
  }
}
