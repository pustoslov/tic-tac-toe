package org.pustoslov.web.model.requests;

import jakarta.validation.constraints.NotBlank;

public record RefreshJwtRequest(@NotBlank String refreshToken) {}
