package com.chat.server.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response<T> {
    private int status;       // HTTP status code
    private String error;     // enum name; null for success
    private String message;   // localized message (optional)
    private T data;           // payload (optional)

    private Response(int status,
                     String error,
                     String message,
                     T data) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public static Response<Void> success(int status,
                                         String message) {
        return new Response<>(status, null, message, null);
    }

    public static <T> Response<T> success(int status,
                                          String message,
                                          T data) {
        return new Response<>(status, null, message, data);
    }

    public static <T> Response<T> error(int status,
                                       String error,
                                       String message) {
        return new Response<>(status, error, message, null);
    }
}
