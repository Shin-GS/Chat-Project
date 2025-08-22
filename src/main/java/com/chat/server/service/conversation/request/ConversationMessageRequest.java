package com.chat.server.service.conversation.request;

import com.chat.server.domain.vo.ConversationId;

public record ConversationMessageRequest(ConversationId conversationId,
                                         String message) {
}
