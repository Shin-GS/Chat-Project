package com.chat.server.service.response;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.dto.ConversationDto;

import java.time.LocalDateTime;

public record ConversationInfoResponse(Long id,
                                       String title,
                                       ConversationType type,
                                       LocalDateTime lastActivityAt) {
    public static ConversationInfoResponse of(ConversationDto conversation) {
        return new ConversationInfoResponse(conversation.id(),
                conversation.title(),
                conversation.type(),
                conversation.lastActivityAt());
    }
}
