package com.chat.server.filter;

import com.chat.server.common.constant.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class LocaleCookieBindingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            Locale fromCookie = resolveLocaleFromCookie(request);
            if (fromCookie != null) {
                LocaleContextHolder.setLocale(fromCookie);
            } else {
                LocaleContextHolder.setLocale(Constants.COOKIE_LOCALE_DEFAULT_VALUE);
            }

            filterChain.doFilter(request, response);

        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    private Locale resolveLocaleFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie c : cookies) {
            if (Constants.COOKIE_LOCALE_NAME.equals(c.getName())) {
                String tag = c.getValue().trim().replace('_', '-');
                return Locale.forLanguageTag(tag); // en, ko-KR
            }
        }

        return null;
    }
}
