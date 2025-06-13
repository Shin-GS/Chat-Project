package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_SAVE_FAILED(400, "User save failed"),
    ;

    private final Integer code;
    private final String message;
}
