package com.chat.server.common.exception;

import com.chat.server.common.Response;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.template.TemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {
    private final TemplateRenderer templateRenderer;

    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(HttpServletRequest request,
                                        CustomException e) {
        log.error("handle CustomException: ", e);
        if (isHxRequest(request)) {
            return convertToastErrorHtml(e.getMessage());
        }

        if (e.getCode() instanceof ErrorCode errorCode) {
            return new ResponseEntity<>(Response.of(errorCode), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(Response.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request,
                                  Exception e) {
        log.error("handle error: ", e);
        if (isHxRequest(request)) {
            return convertToastErrorHtml(e.getMessage());
        }

        return new ResponseEntity<>(Response.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isHxRequest(HttpServletRequest request) {
        return request != null && "true".equals(request.getHeader("HX-Request"));
    }

    private ResponseEntity<String> convertToastErrorHtml(String message) {
        String html = templateRenderer.render(
                "components/toast",
                Map.of("type", "error", "message", message));
        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }
}
