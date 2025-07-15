package com.chat.server.service.payload;

import com.chat.server.domain.entity.Chat;

public record MessagePayload(String to,
                             String from,
                             String message) {
    public static MessagePayload of(Chat chat) {
        return new MessagePayload(chat.getReceiver(), chat.getSender(), chat.getMessage());
    }
}
