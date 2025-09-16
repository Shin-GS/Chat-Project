package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public record ConversationInfoAndMessageResponse(ConversationId id,
                                                 UserId friendUserId,
                                                 String title,
                                                 String imageUrl,
                                                 ConversationType type,
                                                 boolean hasCode,
                                                 LocalDateTime lastActivityAt,
                                                 String lastMessage,
                                                 boolean read) {
    public static ConversationInfoAndMessageResponse ofOneToOne(Conversation conversation,
                                                                UserId friendUserId,
                                                                String title,
                                                                String imageUrl,
                                                                String lastMessage,
                                                                boolean read) {
        return new ConversationInfoAndMessageResponse(conversation.getConversationId(),
                friendUserId,
                title,
                StringUtils.hasText(imageUrl) ? imageUrl : conversation.getImageUrl(),
                conversation.getType(),
                conversation.hasCode(),
                conversation.getLastActivityAt(),
                lastMessage,
                read);
    }

    public static ConversationInfoAndMessageResponse of(Conversation conversation,
                                                        String title,
                                                        String imageUrl,
                                                        String lastMessage,
                                                        boolean read) {
        return new ConversationInfoAndMessageResponse(conversation.getConversationId(),
                null,
                title,
                StringUtils.hasText(imageUrl) ? imageUrl : conversation.getImageUrl(),
                conversation.getType(),
                conversation.hasCode(),
                conversation.getLastActivityAt(),
                lastMessage,
                read);
    }
}
