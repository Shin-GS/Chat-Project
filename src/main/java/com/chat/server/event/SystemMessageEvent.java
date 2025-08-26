package com.chat.server.event;

import com.chat.server.domain.vo.ConversationId;

import java.util.List;

public record SystemMessageEvent(ConversationId conversationId,
                                 Long messageId,
                                 String socketDestination,
                                 List<String> refreshIds) {
    public static SystemMessageEvent of(ConversationId conversationId,
                                        Long messageId,
                                        String socketDestination,
                                        List<String> refreshIds) {
        return new SystemMessageEvent(conversationId, messageId, socketDestination, refreshIds);
    }
}
