package org.pustoslov.web.controller;

import jakarta.validation.Valid;
import org.pustoslov.domain.service.UserService;
import org.pustoslov.web.model.CredentialsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<Void> signUp(@Valid @RequestBody CredentialsRequest request) {
    boolean success = userService.signUp(request);
    if (success) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@Valid @RequestBody CredentialsRequest request) {
    boolean success = userService.authenticate(request.userName(), request.password()) != null;
    if (success) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
