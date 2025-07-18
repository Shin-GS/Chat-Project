package com.chat.server.common.exception;

import com.chat.server.common.Response;
import com.chat.server.common.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(2)
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<ErrorCode>> handleException(Exception e) {
        log.error("handle error: ", e);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(
                Response.of(ErrorCode.INTERNAL_SERVER_ERROR),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
