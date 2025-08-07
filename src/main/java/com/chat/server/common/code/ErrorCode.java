package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    UNAUTHORIZED(401, "Unauthorized"),
    USER_NOT_EXISTS(400, "User not exists"),
    INVALID_PASSWORD(400, "Invalid password"),
    USER_ALREADY_EXISTS(400, "User already exists"),
    USER_SAVE_FAILED(400, "User save failed"),

    TOKEN_EMPTY(400, "Token empty"),
    TOKEN_INVALID(400, "Token invalid"),
    TOKEN_EXPIRED(400, "Token expired"),
    TOKEN_NOT_EXPIRED(400, "Token not expired"),

    CHAT_REQUEST_INVALID(400, "chat request invalid"),
    CHAT_FRIEND_ALREADY_EXISTS(400, "chat friend already exists"),
    CHAT_FRIEND_NOT_EXISTS(400, "chat friend not exists"),
    ;

    private final Integer code;
    private final String message;
}
