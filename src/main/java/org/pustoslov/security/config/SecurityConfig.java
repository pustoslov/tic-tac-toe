package org.pustoslov.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pustoslov.domain.service.UserService;
import org.pustoslov.security.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final ObjectMapper objectMapper;

  public SecurityConfig(UserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthFilter authFilter = new AuthFilter(userService, objectMapper);
    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(authFilter, AuthorizationFilter.class);

    return http.build();
  }
}