package com.chat.server.common.exception;

import com.chat.server.common.code.Code;
import lombok.Getter;

@Getter
public class CustomTokenException extends RuntimeException {
    private final Code code;

    public CustomTokenException(final Code code) {
        super(code.name());
        this.code = code;
    }
}
