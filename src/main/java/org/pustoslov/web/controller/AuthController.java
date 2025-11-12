package org.pustoslov.web.controller;

import jakarta.validation.Valid;
import org.pustoslov.domain.service.AuthService;
import org.pustoslov.web.model.requests.JwtRequest;
import org.pustoslov.web.model.requests.RefreshJwtRequest;
import org.pustoslov.web.model.responses.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<JwtResponse> signUp(@Valid @RequestBody JwtRequest request) {
    JwtResponse response = authService.signUp(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequest request) {
    JwtResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update_access_token")
  public ResponseEntity<JwtResponse> updateAccessToken(
      @Valid @RequestBody RefreshJwtRequest request) {
    JwtResponse response = authService.getNewAccessToken(request.refreshToken());
    return ResponseEntity.ok(response);
  }
}
