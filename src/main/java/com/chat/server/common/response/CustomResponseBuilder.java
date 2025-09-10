package com.chat.server.common.response;

import com.chat.server.common.code.Code;
import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CustomResponseBuilder {
    private final CodeMessageGetter messageGetter;

    public Locale currentLocale() {
        return LocaleContextHolder.getLocale();
    }

    public Response<Void> of(Code code) {
        String message = messageGetter.getMessage(code, currentLocale());
        return (code instanceof SuccessCode) ?
                Response.success(code.getStatus(), message) :
                Response.error(code.getStatus(), code.name(), message);
    }

    public Response<Void> of(Code code, Object... args) {
        String message = messageGetter.getMessage(code, currentLocale(), args);
        return (code instanceof SuccessCode) ?
                Response.success(code.getStatus(), message) :
                Response.error(code.getStatus(), code.name(), message);
    }

    public <T> Response<T> of(Code code, T data) {
        String message = messageGetter.getMessage(code, currentLocale());
        return (code instanceof SuccessCode) ?
                Response.success(code.getStatus(), message, data) :
                Response.error(code.getStatus(), code.name(), message);
    }

    public <T> Response<T> of(Code code, T data, Object... args) {
        String message = messageGetter.getMessage(code, currentLocale(), args);
        return (code instanceof SuccessCode) ?
                Response.success(code.getStatus(), message, data) :
                Response.error(code.getStatus(), code.name(), message);
    }
}
