package com.chat.server.service.conversation.response;

import com.chat.server.domain.entity.converstaion.message.ConverstaionMessage;

public record ConversationMessageResponse(Long id,
                                          String from,
                                          String to,
                                          String message,
                                          boolean mine) {
    public static ConversationMessageResponse of(ConverstaionMessage chat, Long userId) {
        return new ConversationMessageResponse(
                chat.getId(),
                chat.getSenderUsername(),
                chat.getReceiverUsername(),
                chat.getMessage(),
                chat.getSenderUserId().equals(userId));
    }

    public static ConversationMessageResponse of(Long id,
                                                 String from,
                                                 String to,
                                                 String message) {
        return new ConversationMessageResponse(id, from, to, message, false);
    }
}
