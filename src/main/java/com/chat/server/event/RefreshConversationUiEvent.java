package com.chat.server.event;

import com.chat.server.domain.vo.ConversationId;

public record RefreshConversationUiEvent(ConversationId conversationId) {
    public static RefreshConversationUiEvent of(ConversationId conversationId) {
        return new RefreshConversationUiEvent(conversationId);
    }
}
