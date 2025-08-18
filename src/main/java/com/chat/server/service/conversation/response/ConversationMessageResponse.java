package com.chat.server.service.conversation.response;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;

public record ConversationMessageResponse(Long id,
                                          String from,
                                          String message,
                                          boolean mine) {
    public static ConversationMessageResponse ofSender(ConversationMessage message) {
        return new ConversationMessageResponse(message.getId(), message.getSenderUsername(), message.getMessage(), Boolean.TRUE);
    }

    public static ConversationMessageResponse ofReceiver(ConversationMessage message) {
        return new ConversationMessageResponse(message.getId(), message.getSenderUsername(), message.getMessage(), Boolean.FALSE);
    }

    public static ConversationMessageResponse of(ConversationMessage message,
                                                 Long userId) {
        return new ConversationMessageResponse(
                message.getId(),
                message.getSenderUsername(),
                message.getMessage(),
                message.getSenderUserId().equals(userId));
    }
}
