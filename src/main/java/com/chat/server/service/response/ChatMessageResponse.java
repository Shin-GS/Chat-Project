package com.chat.server.service.response;

import com.chat.server.domain.entity.chat.Chat;

public record ChatMessageResponse(Long id,
                                  String from,
                                  String to,
                                  String message,
                                  boolean mine) {
    public static ChatMessageResponse of(Chat chat, Long userId) {
        return new ChatMessageResponse(
                chat.getTId(),
                chat.getSender(),
                chat.getReceiver(),
                chat.getMessage(),
                chat.getSenderUserId().equals(userId));
    }

    public static ChatMessageResponse of(Long id,
                                         String from,
                                         String to,
                                         String message) {
        return new ChatMessageResponse(id, from, to, message, false);
    }
}
