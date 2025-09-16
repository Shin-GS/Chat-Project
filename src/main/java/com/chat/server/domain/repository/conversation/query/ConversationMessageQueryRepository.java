package com.chat.server.domain.repository.conversation.query;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.vo.ConversationId;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ConversationMessageQueryRepository {
    List<ConversationMessage> findBeforeMessages(ConversationId conversationId,
                                                 Long maxMessageId,
                                                 Long joinMessageId,
                                                 Pageable pageable);

    Long findMaxMessageIdByConversationId(ConversationId conversationId);

    Optional<ConversationMessage> findMaxMessageByConversationId(ConversationId conversationId);
}
