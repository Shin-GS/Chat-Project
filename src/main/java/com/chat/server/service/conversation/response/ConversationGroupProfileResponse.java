package com.chat.server.service.conversation.response;

import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.ConversationId;

import java.time.LocalDateTime;

public record ConversationGroupProfileResponse(ConversationId id,
                                               String title,
                                               String profileImageUrl,
                                               LocalDateTime lastActivityAt) {
    public static ConversationGroupProfileResponse of(Conversation conversation) {
        return new ConversationGroupProfileResponse(
                conversation.getConversationId(),
                conversation.getTitle(),
                conversation.getImageUrl(),
                conversation.getLastActivityAt());
    }
}
