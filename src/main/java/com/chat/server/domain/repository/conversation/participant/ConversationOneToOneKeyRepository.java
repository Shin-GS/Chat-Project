package com.chat.server.domain.repository.conversation.participant;

import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import com.chat.server.domain.repository.conversation.query.ConversationOneToOneKeyQueryRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationOneToOneKeyRepository extends JpaRepository<ConversationOneToOneKey, Long>, ConversationOneToOneKeyQueryRepository {
    Optional<String> findOtherUsername(ConversationId conversationId,
                                       UserId userId);

    Optional<Long> findOtherUserId(ConversationId conversationId,
                                   UserId userId);

    Optional<String> findOtherProfileImageUrl(ConversationId conversationId,
                                              UserId userId);
}
