package com.chat.server.common.exception;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.common.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Order(1) // 우선순위 설정: HTMX 요청을 우선 처리
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHxControllerExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public List<ModelAndView> handleBusinessException(HttpServletRequest request,
                                                      CustomException e) {
        if (isNotHxRequest(request)) {
            throw e;
        }

        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "error", "message", e.getMessage()))
                .build();
    }

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request,
                                  Exception e,
                                  Model model) throws Exception {
        if (isNotHxRequest(request)) {
            throw e;
        }

        log.error(e.getMessage(), e);
        model.addAttribute("message", ErrorCode.INTERNAL_SERVER_ERROR.getMessage());

        String fragment = " :: error";
        return "fragments/components/toast" + fragment;
    }

    private boolean isNotHxRequest(HttpServletRequest request) {
        return !"true".equals(request.getHeader("HX-Request"));
    }
}
