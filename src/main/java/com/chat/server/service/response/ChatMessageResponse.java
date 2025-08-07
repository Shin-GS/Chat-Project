package com.chat.server.service.response;

import com.chat.server.domain.entity.Chat;

public record ChatMessageResponse(String from,
                                  String to,
                                  String message) {
    public static ChatMessageResponse of(Chat chat) {
        return new ChatMessageResponse(chat.getSender(), chat.getReceiver(), chat.getMessage());
    }
}
