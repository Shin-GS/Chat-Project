package com.chat.server.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotBlank @NotNull String name,
                                @NotBlank @NotNull String password) {
}
