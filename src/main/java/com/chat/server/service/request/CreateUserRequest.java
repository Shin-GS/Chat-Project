package com.chat.server.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotBlank @NotNull String username,
                                @NotBlank @NotNull String password) {
}
