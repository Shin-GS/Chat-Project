package com.chat.server.domain.repository.conversation.participant;

import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationOneToOneKeyRepository extends JpaRepository<ConversationOneToOneKey, Long> {
    @Query("""
                SELECT conversationUser.username
                FROM ConversationOneToOneKey oneToOneKey, User conversationUser
                    WHERE oneToOneKey.conversationId = :conversationId
                    AND ((:userId = oneToOneKey.smallUserId AND conversationUser.id = oneToOneKey.largeUserId)
                        OR (:userId = oneToOneKey.largeUserId AND conversationUser.id = oneToOneKey.smallUserId))
            """)
    Optional<String> findOtherUsername(@Param("conversationId") Long conversationId,
                                       @Param("userId") Long userId);

    @Query("""
                SELECT conversationUser.id
                FROM ConversationOneToOneKey oneToOneKey, User conversationUser
                    WHERE oneToOneKey.conversationId = :conversationId
                    AND ((:userId = oneToOneKey.smallUserId AND conversationUser.id = oneToOneKey.largeUserId)
                        OR (:userId = oneToOneKey.largeUserId AND conversationUser.id = oneToOneKey.smallUserId))
            """)
    Optional<Long> findOtherUserId(@Param("conversationId") Long conversationId,
                                     @Param("userId") Long userId);
}
