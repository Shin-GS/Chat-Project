package com.chat.server.domain.repository.conversation.message;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {
    @Query("""
            SELECT
               conversationMessage
            FROM ConversationMessage conversationMessage
            WHERE
               conversationMessage.conversationId = :conversationId
               AND (:messageId IS NULL OR conversationMessage.id < :messageId)
            ORDER BY conversationMessage.id DESC
            """)
    List<ConversationMessage> findBeforeMessages(
            @Param("conversationId") Long conversationId,
            @Param("messageId") Long messageId,
            Pageable pageable
    );
}
