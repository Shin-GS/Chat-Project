package com.chat.server.event;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;

public record ConversationMessageEvent(ConversationId conversationId,
                                       UserId fromUserId,
                                       Long messageId) {
    public static ConversationMessageEvent of(ConversationId conversationId,
                                              UserId fromUserId,
                                              Long messageId) {
        return new ConversationMessageEvent(conversationId, fromUserId, messageId);
    }
}
