package com.chat.server.service.auth.request;

import com.chat.server.common.constant.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank
                           @Size(max = Constants.USER_ID_MAX_LENGTH)
                           String accountId,

                           @NotBlank
                           @Size(max = Constants.USER_HASHED_PASSWORD_MAX_LENGTH)
                           String password) {
}
