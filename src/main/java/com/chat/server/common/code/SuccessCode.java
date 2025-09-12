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
    USER_LOGGED_OUT(HttpStatus.OK),
    USER_INFO_RETRIEVED(HttpStatus.OK),
    USER_PROFILE_UPDATED(HttpStatus.OK),

    FRIEND_ADDED(HttpStatus.OK),
    FRIEND_DELETED(HttpStatus.OK),

    CONVERSATION_GROUP_CREATED(HttpStatus.CREATED),
    CONVERSATION_ONE_TO_ONE_JOINED(HttpStatus.OK),
    CONVERSATION_GROUP_JOINED(HttpStatus.OK),
    CONVERSATION_LEFT(HttpStatus.OK),
    CONVERSATION_MESSAGE_RETRIEVED(HttpStatus.OK),
    CONVERSATION_GROUP_ROLE_UPDATED(HttpStatus.OK),
    ;

    private final HttpStatus httpStatus;
}
