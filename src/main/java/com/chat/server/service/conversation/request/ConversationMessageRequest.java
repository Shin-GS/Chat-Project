package com.chat.server.service.conversation.request;

public record ConversationMessageRequest(Long conversationId,
                                         String message) {
}
