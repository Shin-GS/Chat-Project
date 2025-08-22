package com.chat.server.domain.repository.conversation.participant;

import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import com.chat.server.domain.vo.UserId;
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
                    AND ((:#{#userId.value} = oneToOneKey.smallUserId.value AND conversationUser.id = oneToOneKey.largeUserId.value)
                        OR (:#{#userId.value} = oneToOneKey.largeUserId.value AND conversationUser.id = oneToOneKey.smallUserId.value))
            """)
    Optional<String> findOtherUsername(@Param("conversationId") Long conversationId,
                                       @Param("userId") UserId userId);

    @Query("""
                SELECT conversationUser.id
                FROM ConversationOneToOneKey oneToOneKey, User conversationUser
                    WHERE oneToOneKey.conversationId = :conversationId
                    AND ((:#{#userId.value} = oneToOneKey.smallUserId.value AND conversationUser.id = oneToOneKey.largeUserId.value)
                        OR (:#{#userId.value} = oneToOneKey.largeUserId.value AND conversationUser.id = oneToOneKey.smallUserId.value))
            """)
    Optional<Long> findOtherUserId(@Param("conversationId") Long conversationId,
                                     @Param("userId") UserId userId);
}
