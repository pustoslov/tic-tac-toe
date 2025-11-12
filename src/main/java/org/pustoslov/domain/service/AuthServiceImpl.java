package org.pustoslov.domain.service;

import java.util.Optional;
import java.util.UUID;
import org.pustoslov.config.jwt.JwtProvider;
import org.pustoslov.config.security.JwtAuthentication;
import org.pustoslov.domain.exception.AuthenticationException;
import org.pustoslov.domain.model.User;
import org.pustoslov.web.model.JwtRequest;
import org.pustoslov.web.model.JwtResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final JwtProvider jwtProvider;

  public AuthServiceImpl(UserService userService, JwtProvider jwtProvider) {
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public JwtResponse login(JwtRequest request) {
    User user = userService.authenticate(request.login(), request.password());
    if (user == null) throw new AuthenticationException("Incorrect login or password.");
    String accessToken = jwtProvider.generateAccessToken(user);
    String refreshToken = jwtProvider.generateRefreshToken(user);
    return new JwtResponse("Bearer", accessToken, refreshToken);
  }

  @Override
  public JwtResponse signUp(JwtRequest request) {
    User user = userService.signUp(request);
    String accessToken = jwtProvider.generateAccessToken(user);
    String refreshToken = jwtProvider.generateRefreshToken(user);
    return new JwtResponse("Bearer", accessToken, refreshToken);
  }

  @Override
  public JwtResponse getNewAccessToken(String refreshToken) {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      UUID uuid = jwtProvider.getUuidFromRefreshToken(refreshToken);
      User user = userService.findUserById(uuid);
      String accessToken = jwtProvider.generateAccessToken(user);
      return new JwtResponse("Bearer", accessToken, refreshToken);
    } else throw new AuthenticationException("Invalid or expired refresh token.");
  }

  @Override
  public JwtAuthentication getAuthInfo() {
    return getAuthInfoSafely()
        .orElseThrow(() -> new AuthenticationException("User not authenticated"));
  }

  private Optional<JwtAuthentication> getAuthInfoSafely() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(authentication -> authentication instanceof JwtAuthentication)
        .map(authentication -> (JwtAuthentication) authentication);
  }
}
