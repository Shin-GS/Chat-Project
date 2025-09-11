package com.chat.server.common.constant.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConversationType {
    ONE_TO_ONE("1:1 Chat"),
    GROUP("Group Chat")
    // Additional types may be added, e.g., Inquiry, Advertisement, etc.
    ;

    private final String description;

    public static ConversationType from(String roleName) {
        if (roleName == null) {
            return null;
        }

        try {
            return ConversationType.valueOf(roleName);

        } catch (Exception e) {
            return null;
        }
    }
}
