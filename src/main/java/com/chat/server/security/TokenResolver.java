package com.chat.server.security;

import com.chat.server.common.constant.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenResolver {
    private final HttpServletRequest request;

    public String resolveAccessToken() {
        String token = request.getHeader(Constants.HEADER_AUTHORIZATION);
        return extractToken(token);
    }

    public String resolveRefreshToken() {
        String token = request.getHeader(Constants.HEADER_AUTHORIZATION_REFRESH);
        return extractToken(token);
    }

    private String extractToken(String raw) {
        if (!StringUtils.hasText(raw) || !raw.startsWith(Constants.BEARER_PREFIX)) {
            return null;
        }

        return raw.substring(Constants.BEARER_PREFIX_LENGTH);
    }
}
