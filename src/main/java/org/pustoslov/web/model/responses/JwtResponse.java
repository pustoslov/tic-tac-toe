package org.pustoslov.web.model.responses;

public record JwtResponse(String type, String accessToken, String refreshToken) {}
