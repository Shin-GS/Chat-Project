package com.chat.server.common.code;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CodeMessageGetter {
    private final MessageSource messageSource;

    public String getMessage(Code code,
                             Locale locale) {
        return (code instanceof SuccessCode) ?
                messageSource.getMessage("success." + code.name(), null, code.name(), locale) :
                messageSource.getMessage("error." + code.name(), null, code.name(), locale);
    }

    public String getMessage(Code code,
                             Locale locale,
                             Object... args) {
        return (code instanceof SuccessCode) ?
                messageSource.getMessage("success." + code.name(), args, code.name(), locale) :
                messageSource.getMessage("error." + code.name(), args, code.name(), locale);
    }
}
