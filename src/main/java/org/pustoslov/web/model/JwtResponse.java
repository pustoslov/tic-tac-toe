package org.pustoslov.web.model;

public record JwtResponse(String type, String accessToken, String refreshToken) {}
