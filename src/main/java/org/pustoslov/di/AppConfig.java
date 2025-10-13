package org.pustoslov.di;

import org.pustoslov.datasource.mapper.GameDatasourceMapper;
import org.pustoslov.datasource.repository.GameRepository;
import org.pustoslov.datasource.repository.UserRepository;
import org.pustoslov.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Bean
  public GameService gameService(
      GameRepository gameRepository,
      UserRepository userRepository,
      GameDatasourceMapper mapper,
      TicTacToeEngine engine) {
    return new GameServiceImpl(gameRepository, userRepository, mapper, engine);
  }

  @Bean
  public UserService userService(UserRepository repository, PasswordEncoder encoder) {
    return new UserServiceImpl(repository, encoder);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
