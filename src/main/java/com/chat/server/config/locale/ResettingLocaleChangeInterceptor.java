package com.chat.server.config.locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

public class ResettingLocaleChangeInterceptor extends LocaleChangeInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws ServletException {
        LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
        if (resolver == null) {
            return true;
        }

        String paramName = getParamName(); // lang
        String raw = request.getParameter(paramName);

        // param ABSENT -> keep cookie; do not touch resolver
        if (raw == null) {
            return true;
        }

        // param PRESENT but EMPTY -> clear cookie -> use default locale from resolver
        if (raw.isBlank()) {
            resolver.setLocale(request, response, null); // CookieLocaleResolver: removes cookie
            return true;
        }

        // param PRESENT with value -> normal behavior (will call parseLocaleValue)
        return super.preHandle(request, response, handler);
    }

    @Override
    protected Locale parseLocaleValue(@NonNull String localeValue) {
        String tag = localeValue.trim().replace('_', '-'); // Normalize: "ko_kR", "ko-kr" -> "ko-KR"
        return Locale.forLanguageTag(tag); // BCP47-safe parsing; works for "ko" and "en-US"
    }
}
