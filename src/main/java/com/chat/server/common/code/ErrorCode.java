package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_SAVE_FAILED(400, "User save failed"),
    USER_NOT_EXISTS(400, "User not exists"),
    INVALID_PASSWORD(400, "Invalid password"),
    ;

    private final Integer code;
    private final String message;
}
