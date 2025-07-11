package com.chat.server.security;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenResolver {
    private final HttpServletRequest request;

    public String resolveAccessToken() {
        String token = request.getHeader(Constants.HEADER_AUTHORIZATION);
        return extractTokenOrThrow(token, Constants.HEADER_AUTHORIZATION);
    }

    public String resolveRefreshToken() {
        String token = request.getHeader(Constants.HEADER_AUTHORIZATION_REFRESH);
        return extractTokenOrThrow(token, Constants.HEADER_AUTHORIZATION_REFRESH);
    }

    private String extractTokenOrThrow(String raw, String headerName) {
        if (raw == null || raw.isBlank()) {
            log.warn("Missing token in header '{}'", headerName);
            throw new CustomException(
                    ErrorCode.TOKEN_EMPTY,
                    "%s header is empty".formatted(headerName)
            );
        }

        if (!raw.startsWith(Constants.BEARER_PREFIX)) {
            log.warn("Invalid token format in header '{}': {}", headerName, raw);
            throw new CustomException(
                    ErrorCode.TOKEN_INVALID,
                    "%s must start with '%s'".formatted(headerName, Constants.BEARER_PREFIX)
            );
        }

        return raw.substring(Constants.BEARER_PREFIX_LENGTH);
    }
}
