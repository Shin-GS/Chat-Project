package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode implements Code {
    SUC(200, "Success"),

    USER_CREATED(201, "User registration successful"),
    USER_LOGGED_IN(200, "Login successful"),
    USER_INFO_RETRIEVED(200, "User information retrieved successfully"),

    CONVERSATION_MESSAGE_RETRIEVED(200, "Conversation messages retrieved successfully"),
    ;

    private final Integer code;
    private final String message;
}
