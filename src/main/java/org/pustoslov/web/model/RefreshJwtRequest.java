package org.pustoslov.web.model;

import jakarta.validation.constraints.NotBlank;

public record RefreshJwtRequest(@NotBlank String refreshToken) {}
