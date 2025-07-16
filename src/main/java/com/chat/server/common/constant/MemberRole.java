package com.chat.server.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberRole {
    USER("일반회원"),
    ADMIN("운영자");

    private final String description;

    public static MemberRole from(String name) {
        if (name == null) {
            return null;
        }

        try {
            return MemberRole.valueOf(name);

        } catch (Exception e) {
            return null;
        }
    }
}
