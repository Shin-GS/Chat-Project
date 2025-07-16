package com.chat.server.security;

import com.chat.server.common.constant.MemberRole;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public record JwtMemberInfo(Long id,
                            String username,
                            MemberRole role) {
    public static JwtMemberInfo of(Long id,
                                   String username,
                                   MemberRole role) {
        return new JwtMemberInfo(id, username, role);
    }
}
