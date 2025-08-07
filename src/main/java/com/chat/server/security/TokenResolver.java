package com.chat.server.security;

import com.chat.server.common.constant.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TokenResolver {
    private final HttpServletRequest request;

    public String resolveAccessToken() {
        return extractTokenFromCookie(Constants.COOKIE_AUTHORIZATION);
    }

    public String resolveRefreshToken() {
        return extractTokenFromCookie(Constants.COOKIE_AUTHORIZATION_REFRESH);
    }

    private String extractTokenFromCookie(String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
