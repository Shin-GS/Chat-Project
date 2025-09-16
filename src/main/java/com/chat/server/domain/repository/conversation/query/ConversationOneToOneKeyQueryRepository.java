package com.chat.server.domain.repository.conversation.query;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;

import java.util.Optional;

public interface ConversationOneToOneKeyQueryRepository {
    Optional<String> findOtherUsername(ConversationId conversationId,
                                       UserId userId);

    Optional<Long> findOtherUserId(ConversationId conversationId,
                                   UserId userId);

    Optional<String> findOtherProfileImageUrl(ConversationId conversationId,
                                              UserId userId);
}
