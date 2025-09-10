package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode implements Code {
    SUC(HttpStatus.OK),

    USER_CREATED(HttpStatus.CREATED),
    USER_LOGGED_IN(HttpStatus.OK),
    USER_INFO_RETRIEVED(HttpStatus.OK),

    CONVERSATION_MESSAGE_RETRIEVED(HttpStatus.OK),
    ;

    private final HttpStatus httpStatus;
}
