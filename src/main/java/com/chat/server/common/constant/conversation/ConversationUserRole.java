package com.chat.server.common.constant.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConversationUserRole {
    SUPER_ADMIN("SUPER ADMIN"),
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String description;

    public static ConversationUserRole from(String roleName) {
        if (roleName == null) {
            return null;
        }

        try {
            return ConversationUserRole.valueOf(roleName);

        } catch (Exception e) {
            return null;
        }
    }

    public static int sortPriority(ConversationUserRole role) {
        return switch (role) {
            case SUPER_ADMIN -> 0;
            case ADMIN -> 1;
            case MEMBER -> 2;
        };
    }
}
