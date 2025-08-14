package com.chat.server.service.conversation.request;

public record ConversationMessageRequest(Long userId,
                                         String message) {
}
