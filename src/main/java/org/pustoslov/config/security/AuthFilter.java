package org.pustoslov.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.pustoslov.config.jwt.JwtProvider;
import org.pustoslov.config.jwt.JwtUtil;
import org.pustoslov.web.model.responses.ErrorResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthFilter extends GenericFilterBean {

  private final JwtProvider jwtProvider;
  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper;

  public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil, ObjectMapper objectMapper) {
    this.jwtProvider = jwtProvider;
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (isPublicEndpoint(httpRequest)) {
      chain.doFilter(request, response);
      return;
    }

    String token = extractToken(httpRequest, httpResponse);
    if (token == null) {
      return;
    }

    if (!jwtProvider.validateAccessToken(token)) {
      sendErrorResponse(httpResponse, "Invalid or expired access token");
      return;
    }

    try {
      Claims claims = jwtProvider.parseToken(token);
      JwtAuthentication authentication = jwtUtil.generateAuth(claims);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    } catch (Exception e) {
      sendErrorResponse(httpResponse, "Failed to process JWT token");
    } finally {
      SecurityContextHolder.clearContext();
    }
  }

  private String extractToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      sendErrorResponse(response, "Missing or invalid Authorization header");
      return null;
    }
    return authHeader.substring("Bearer ".length());
  }

  private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    ErrorResponse error = new ErrorResponse("Authentication error", message);
    objectMapper.writeValue(response.getWriter(), error);
  }

  private boolean isPublicEndpoint(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();
    return ("/api/v1/auth/signup".equals(path) && "POST".equalsIgnoreCase(method))
        || ("/api/v1/auth/login".equals(path) && "POST".equalsIgnoreCase(method))
        || ("/api/v1/auth/update_access_token".equals(path) && "POST".equalsIgnoreCase(method))
        || ("/api/v1/auth/update_refresh_token".equals(path) && "POST".equalsIgnoreCase(method));
  }
}
