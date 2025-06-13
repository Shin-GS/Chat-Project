package com.chat.server.common;

import com.chat.server.common.code.Code;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final Code code;

    public CustomException(final Code code) {
        super(code.getMessage());
        this.code = code;
    }
}
