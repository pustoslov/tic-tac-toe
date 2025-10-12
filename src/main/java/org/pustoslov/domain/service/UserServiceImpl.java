package org.pustoslov.domain.service;

import org.pustoslov.datasource.model.UserEntity;
import org.pustoslov.datasource.repository.UserRepository;
import org.pustoslov.web.model.CredentialsRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public boolean signUp(CredentialsRequest request) {
    if (userRepository.existsByUsername(request.userName())) {
      return false;
    }
    String encodedPassword = passwordEncoder.encode(request.password());
    UserEntity entity = new UserEntity(request.userName(), encodedPassword);
    userRepository.save(entity);
    return true;
  }

  @Override
  public UUID authenticate(String userName, String password) {
    Optional<UserEntity> userOpt = userRepository.findByUsername(userName);

    if (userOpt.isEmpty()) {
      return null;
    }

    UserEntity user = userOpt.get();

    if (passwordEncoder.matches(password, user.getPassword())) {
      return user.getId();
    }

    return null;
  }

  @Override
  public String findUserById(UUID userId) {
    UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
    return entity.getUsername();
  }
}
