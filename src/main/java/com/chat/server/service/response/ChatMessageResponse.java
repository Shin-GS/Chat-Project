package com.chat.server.service.response;

import com.chat.server.domain.entity.Chat;

public record ChatMessageResponse(String from,
                                  String to,
                                  String message,
                                  boolean mine) {
    public static ChatMessageResponse of(Chat chat, Long userId) {
        return new ChatMessageResponse(
                chat.getSender(),
                chat.getReceiver(),
                chat.getMessage(),
                chat.getSenderUserId().equals(userId));
    }

    public static ChatMessageResponse of(String from,
                                         String to,
                                         String message) {
        return new ChatMessageResponse(from, to, message, false);
    }
}
