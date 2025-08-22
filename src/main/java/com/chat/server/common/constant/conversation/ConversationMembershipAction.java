package com.chat.server.common.constant.conversation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConversationMembershipAction {
    JOIN("참가"),
    LEAVE("나가기")
    ;

    private final String description;

    public static ConversationMembershipAction from(String roleName) {
        if (roleName == null) {
            return null;
        }

        try {
            return ConversationMembershipAction.valueOf(roleName);

        } catch (Exception e) {
            return null;
        }
    }
}
