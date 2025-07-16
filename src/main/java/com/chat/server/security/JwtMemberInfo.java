package com.chat.server.security;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record JwtMemberInfo(Long id,
                            String username) {
    public static JwtMemberInfo of(Long id, String username) {
        return new JwtMemberInfo(id, username);
    }
}
