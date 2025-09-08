package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.ConversationId;

import java.time.LocalDateTime;

public record ConversationInfoAndMessageResponse(ConversationId id,
                                                 String title,
                                                 ConversationType type,
                                                 boolean hasCode,
                                                 LocalDateTime lastActivityAt,
                                                 String lastMessage,
                                                 boolean read) {
    public static ConversationInfoAndMessageResponse of(Conversation conversation,
                                                        String title,
                                                        String lastMessage,
                                                        boolean read) {
        return new ConversationInfoAndMessageResponse(conversation.getConversationId(),
                title,
                conversation.getType(),
                conversation.hasCode(),
                conversation.getLastActivityAt(),
                lastMessage,
                read);
    }
}
