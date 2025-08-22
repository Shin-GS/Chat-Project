package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.ConversationId;

import java.time.LocalDateTime;

public record ConversationInfoResponse(ConversationId id,
                                       String title,
                                       ConversationType type,
                                       boolean hasCode,
                                       LocalDateTime lastActivityAt) {
    public static ConversationInfoResponse of(ConversationDto conversation) {
        return new ConversationInfoResponse(ConversationId.of(conversation.id()),
                conversation.title(),
                conversation.type(),
                conversation.hasCode(),
                conversation.lastActivityAt());
    }

    public static ConversationInfoResponse of(Conversation conversation,
                                              String title) {
        return new ConversationInfoResponse(conversation.getConversationId(),
                title,
                conversation.getType(),
                conversation.hasCode(),
                conversation.getLastActivityAt());
    }
}
