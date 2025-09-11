package com.chat.server.common.code;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CodeMessageGetter {
    private final MessageSource messageSource;

    public Locale currentLocale() {
        return LocaleContextHolder.getLocale();
    }

    public String getMessage(Code code) {
        Locale locale = currentLocale();
        return (code instanceof SuccessCode) ?
                messageSource.getMessage("success." + code.name(), null, code.name(), locale) :
                messageSource.getMessage("error." + code.name(), null, code.name(), locale);
    }

    public String getMessage(Code code,
                             Object... args) {
        Locale locale = currentLocale();
        return (code instanceof SuccessCode) ?
                messageSource.getMessage("success." + code.name(), args, code.name(), locale) :
                messageSource.getMessage("error." + code.name(), args, code.name(), locale);
    }
}
