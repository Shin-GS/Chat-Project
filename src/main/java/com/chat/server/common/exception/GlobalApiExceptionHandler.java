package com.chat.server.common.exception;

import com.chat.server.common.Response;
import com.chat.server.common.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler {
    private GlobalApiExceptionHandler() {
    }

    @ExceptionHandler(CustomException.class)
    protected Response<Object> handleCustomException(CustomException e) {
        log.error("handle CustomException: ", e);
        if (e.getCode() instanceof ErrorCode errorCode) {
            return Response.of(errorCode);
        }

        return Response.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected Response<Object> handleException(Exception e) {
        log.error("handle error: ", e);
        return Response.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
