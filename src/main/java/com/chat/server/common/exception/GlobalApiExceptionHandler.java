package com.chat.server.common.exception;

import com.chat.server.common.Response;
import com.chat.server.common.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(2)
@RestControllerAdvice
public class GlobalApiExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<String>> handleCustomException(CustomException e) {
        log.error("handle CustomException: ", e);
        if (e.getCode() instanceof ErrorCode errorCode) {
            return new ResponseEntity<>(Response.of(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(Response.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleException(Exception e) {
        log.error("handle error: ", e);
        return new ResponseEntity<>(Response.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
