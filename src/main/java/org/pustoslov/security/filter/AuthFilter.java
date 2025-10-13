package org.pustoslov.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.pustoslov.domain.service.UserService;
import org.pustoslov.web.model.ErrorResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class AuthFilter extends GenericFilterBean {

  private final UserService userService;
  private final ObjectMapper objectMapper;

  public AuthFilter(UserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
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

    String[] credentials = extractCredentials(httpRequest, httpResponse);
    if (credentials == null) {
      return;
    }

    UUID userId = userService.authenticate(credentials[0], credentials[1]);

    if (userId == null) {
      sendErrorResponse(httpResponse, "Authentication failed");
    } else {
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userId, null, List.of());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      chain.doFilter(request, response);
    }
  }

  private String[] extractCredentials(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Basic ")) {
      sendErrorResponse(response, "Missing or invalid Authorization header");
      return null;
    }

    String base64Credentials = authHeader.substring("Basic ".length());
    String credentials =
        new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);

    String[] values = credentials.split(":", 2);
    if (values.length != 2) {
      sendErrorResponse(response, "Invalid Basic Auth format");
      return null;
    }

    return values;
  }

  private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    ErrorResponse error = new ErrorResponse("Authentification error", message);
    objectMapper.writeValue(response.getWriter(), error);
  }

  private boolean isPublicEndpoint(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();
    return ("/api/v1/auth/signup".equals(path) && "POST".equalsIgnoreCase(method))
        || ("/api/v1/auth/login".equals(path) && "POST".equalsIgnoreCase(method));
  }
}
