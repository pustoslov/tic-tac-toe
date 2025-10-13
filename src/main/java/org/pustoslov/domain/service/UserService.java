package org.pustoslov.domain.service;

import java.util.UUID;
import org.pustoslov.web.model.CredentialsRequest;

public interface UserService {
  boolean signUp(CredentialsRequest request);

  UUID authenticate(String userName, String password);

  String findUserById(UUID userId);
}
