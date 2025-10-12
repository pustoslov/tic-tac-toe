package org.pustoslov.domain.service;

import org.pustoslov.web.model.CredentialsRequest;

import java.util.UUID;

public interface UserService {
  boolean signUp(CredentialsRequest request);
  UUID authenticate(String userName, String password);
  String findUserById(UUID userId);
}
