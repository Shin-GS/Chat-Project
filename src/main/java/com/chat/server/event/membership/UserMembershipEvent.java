package com.chat.server.event.membership;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;

public record UserMembershipEvent(ConversationId conversationId,
                                  UserId userId,
                                  Long messageId) {
    public static UserMembershipEvent of(ConversationId conversationId,
                                         UserId userId,
                                         Long messageId) {
        return new UserMembershipEvent(conversationId, userId, messageId);
    }
}
