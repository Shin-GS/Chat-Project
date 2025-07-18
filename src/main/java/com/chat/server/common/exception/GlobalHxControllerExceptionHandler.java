package com.chat.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Order(1) // 우선순위 설정: HTMX 요청을 우선 처리
@ControllerAdvice
public class GlobalHxControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request,
                                  Exception e,
                                  Model model) throws Exception {
        log.error("handle Exception: ", e);
        if (isNotHxRequest(request)) {
            throw e;
        }

        model.addAttribute("type", "error");
        model.addAttribute("message", e.getMessage());
        return "components/toast :: message";
    }

    private boolean isNotHxRequest(HttpServletRequest request) {
        return !"true".equals(request.getHeader("HX-Request"));
    }
}
