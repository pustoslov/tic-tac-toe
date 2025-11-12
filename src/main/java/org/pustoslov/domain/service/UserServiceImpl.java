package org.pustoslov.domain.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.pustoslov.datasource.mapper.UserMapper;
import org.pustoslov.datasource.model.UserEntity;
import org.pustoslov.datasource.repository.UserRepository;
import org.pustoslov.domain.exception.UserAlreadyExists;
import org.pustoslov.domain.model.Role;
import org.pustoslov.domain.model.RoleName;
import org.pustoslov.domain.model.User;
import org.pustoslov.web.model.JwtRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserServiceImpl(
      UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  @Override
  public User signUp(JwtRequest request) {
    if (userRepository.existsByUsername(request.login())) {
      throw new UserAlreadyExists("User: " + request.login() + " already exists.");
    }
    String encodedPassword = passwordEncoder.encode(request.password());
    User user = new User(request.login(), encodedPassword, Set.of(new Role(RoleName.USER)));
    return userMapper.toDomain(userRepository.save(userMapper.toDataEntity(user)));
  }

  @Override
  public User authenticate(String username, String password) {
    Optional<UserEntity> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) {
      return null;
    }

    UserEntity user = userOpt.get();
    if (passwordEncoder.matches(password, user.getPassword())) {
      return userMapper.toDomain(user);
    }

    return null;
  }

  @Override
  public User findUserById(UUID userId) {
    UserEntity entity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("No user with id: " + userId));
    return userMapper.toDomain(entity);
  }
}
