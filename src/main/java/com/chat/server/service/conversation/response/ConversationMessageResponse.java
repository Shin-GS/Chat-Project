package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.vo.UserId;

import java.time.LocalDateTime;

public record ConversationMessageResponse(Long id,
                                          ConversationMessageType type,
                                          String from,
                                          String message,
                                          LocalDateTime createdAt,
                                          boolean mine) {
    public static ConversationMessageResponse ofSender(ConversationMessage message) {
        return new ConversationMessageResponse(
                message.getId(),
                message.getType(),
                message.getSenderUsername(),
                message.getMessage(),
                message.getCreatedAt(),
                Boolean.TRUE);
    }

    public static ConversationMessageResponse ofReceiver(ConversationMessage message) {
        return new ConversationMessageResponse(
                message.getId(),
                message.getType(),
                message.getSenderUsername(),
                message.getMessage(),
                message.getCreatedAt(),
                Boolean.FALSE);
    }

    public static ConversationMessageResponse of(ConversationMessage message,
                                                 UserId userId) {
        return new ConversationMessageResponse(
                message.getId(),
                message.getType(),
                message.getSenderUsername(),
                message.getMessage(),
                message.getCreatedAt(),
                message.getSenderUserId().equals(userId));
    }
}
