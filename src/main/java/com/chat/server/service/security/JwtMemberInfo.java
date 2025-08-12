package com.chat.server.service.security;

import com.chat.server.common.constant.UserRole;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record JwtMemberInfo(Long id,
                            String accountId,
                            String username,
                            UserRole role) {
    public static JwtMemberInfo of(Long id,
                                   String accountId,
                                   String username,
                                   UserRole role) {
        return new JwtMemberInfo(id, accountId, username, role);
    }
}
