package org.pustoslov.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record JwtRequest(
    @NotNull
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String login,
    @NotNull
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message =
                "Password must contain at least one uppercase letter, "
                    + "one lowercase letter, and one number")
        String password) {}
