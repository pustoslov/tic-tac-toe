package org.pustoslov.domain.service;

import java.util.UUID;
import org.pustoslov.domain.model.User;
import org.pustoslov.web.model.JwtRequest;

public interface UserService {
  User signUp(JwtRequest request);

  User authenticate(String username, String password);

  User findUserById(UUID userId);
}
