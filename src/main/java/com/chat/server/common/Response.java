package com.chat.server.common;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.code.SuccessCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response<T> {
    private int code;
    private String message;
    private T data;

    private Response(int code,
                     String message,
                     T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response<Object> of(SuccessCode code) {
        return new Response<>(code.getCode(), code.getMessage(), null);
    }

    public static <T> Response<T> of(SuccessCode code,
                                     T data) {
        return new Response<>(code.getCode(), code.getMessage(), data);
    }

    public static <T> Response<T> of(ErrorCode code) {
        return new Response<>(code.getCode(), code.getMessage(), null);
    }
}
