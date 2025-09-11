package com.chat.server.common.exception;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.response.CustomResponseBuilder;
import com.chat.server.common.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Order(1)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalApiExceptionHandler {
    private final CustomResponseBuilder responseBuilder;
    private final CodeMessageGetter codeMessageGetter;

    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(HttpServletRequest request,
                                        CustomException e) {
        log.error("handle CustomException: ", e);
        if (RequestUtil.isHxRequest(request)) {
            String errorMessage = codeMessageGetter.getMessage(e.getCode(), codeMessageGetter.currentLocale());
            return toastFragmentView(errorMessage);
        }

        if (e.getCode() instanceof ErrorCode errorCode) {
            return responseBuilder.of(errorCode);
        }

        return new ResponseEntity<>(responseBuilder.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request,
                                  Exception e) {
        log.error("handle exception: ", e);
        if (RequestUtil.isHxRequest(request)) {
            return toastFragmentView(e.getMessage());
        }

        return new ResponseEntity<>(responseBuilder.of(ErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelAndView toastFragmentView(String message) {
        ModelAndView mv = new ModelAndView("components/common/toast :: message");
        mv.addObject("type", "error");
        mv.addObject("message", message);
        return mv;
    }
}
