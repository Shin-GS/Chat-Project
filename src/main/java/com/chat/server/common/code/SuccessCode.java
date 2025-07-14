package com.chat.server.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode implements Code {
    Success(200, "Success"),
    ;

    private final Integer code;
    private final String message;
}
