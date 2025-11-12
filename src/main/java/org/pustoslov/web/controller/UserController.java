package org.pustoslov.web.controller;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.pustoslov.domain.exception.IllegalRequestParameter;
import org.pustoslov.domain.service.AuthService;
import org.pustoslov.domain.service.GameService;
import org.pustoslov.domain.service.UserService;
import org.pustoslov.web.mapper.RatingStatsMapper;
import org.pustoslov.web.mapper.UserDataMapper;
import org.pustoslov.web.model.responses.RatingStatsResponse;
import org.pustoslov.web.model.responses.UserDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
  private final UserService userService;
  private final AuthService authService;
  private final GameService gameService;
  private final RatingStatsMapper ratingStatsMapper;
  private final UserDataMapper userDataMapper;

  public UserController(
      UserService userService,
      AuthService authService,
      GameService gameService,
      RatingStatsMapper ratingStatsMapper,
      UserDataMapper userDataMapper) {
    this.userService = userService;
    this.authService = authService;
    this.gameService = gameService;
    this.ratingStatsMapper = ratingStatsMapper;
    this.userDataMapper = userDataMapper;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDataResponse> getUsername(@PathVariable UUID userId) {
    UserDataResponse response = userDataMapper.toDTO(userService.findUserById(userId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<UserDataResponse> getMyData() {
    UUID id = UUID.fromString(authService.getAuthInfo().getName());
    UserDataResponse response = userDataMapper.toDTO(userService.findUserById(id));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/top")
  public ResponseEntity<List<RatingStatsResponse>> getTop(
      @RequestParam @NotNull(message = "Parameter 'limit' is required") String limit) {
    int limitInteger;
    try {
      limitInteger = Integer.parseInt(limit);
    } catch (NumberFormatException e) {
      throw new IllegalRequestParameter("'limit' must be a number.");
    }
    return ResponseEntity.ok(
        gameService.getTopPlayers(limitInteger).stream().map(ratingStatsMapper::toDTO).toList());
  }
}
