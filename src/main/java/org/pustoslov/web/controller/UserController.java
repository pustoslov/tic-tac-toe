package org.pustoslov.web.controller;

import java.util.UUID;
import org.pustoslov.domain.service.UserService;
import org.pustoslov.web.model.UserDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDataResponse> getUsername(@PathVariable UUID userId) {
    UserDataResponse response = new UserDataResponse(userService.findUserById(userId));
    return ResponseEntity.ok(response);
  }
}
