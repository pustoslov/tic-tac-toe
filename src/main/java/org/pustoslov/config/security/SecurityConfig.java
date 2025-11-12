package org.pustoslov.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pustoslov.config.jwt.JwtProvider;
import org.pustoslov.config.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper;

  public SecurityConfig(JwtProvider jwtProvider, JwtUtil jwtUtil, ObjectMapper objectMapper) {
    this.jwtProvider = jwtProvider;
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthFilter authFilter = new AuthFilter(jwtProvider, jwtUtil, objectMapper);
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
        .addFilterBefore(authFilter, AuthorizationFilter.class);

    return http.build();
  }
}
