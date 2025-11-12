package org.pustoslov.domain.service;

import org.pustoslov.config.security.JwtAuthentication;
import org.pustoslov.web.model.JwtRequest;
import org.pustoslov.web.model.JwtResponse;

public interface AuthService {
  JwtResponse login(JwtRequest request);

  JwtResponse signUp(JwtRequest request);

  JwtResponse getNewAccessToken(String refreshToken);

  JwtResponse getNewRefreshToken(String refreshToken);

  JwtAuthentication getAuthInfo();
}
