package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.entity.converstaion.sticker.Sticker;

import java.time.LocalDateTime;

public record ConversationMessageResponse(Long id,
                                          ConversationMessageType type,
                                          String from,
                                          String message,
                                          String imageUrl,
                                          LocalDateTime createdAt,
                                          boolean mine) {
    public static ConversationMessageResponse ofText(ConversationMessage message,
                                                     boolean isSender) {
        return new ConversationMessageResponse(
                message.getId(),
                ConversationMessageType.TEXT,
                message.getSenderUsername(),
                message.getMessage(),
                null,
                message.getCreatedAt(),
                isSender);
    }

    public static ConversationMessageResponse ofSystem(ConversationMessage message) {
        return new ConversationMessageResponse(
                message.getId(),
                ConversationMessageType.SYSTEM,
                message.getSenderUsername(),
                message.getMessage(),
                null,
                message.getCreatedAt(),
                Boolean.FALSE);
    }

    public static ConversationMessageResponse ofSticker(ConversationMessage message,
                                                        Sticker sticker,
                                                        boolean isSender) {
        return new ConversationMessageResponse(
                message.getId(),
                ConversationMessageType.STICKER,
                message.getSenderUsername(),
                sticker.getAltText(),
                sticker.getImageUrl(),
                message.getCreatedAt(),
                isSender);
    }
}
