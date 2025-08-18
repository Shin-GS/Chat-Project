package com.chat.server.domain.repository.conversation.message;

import com.chat.server.domain.entity.converstaion.message.ConverstaionMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConverstaionMessage, Long> {
    @Query("""
            SELECT
               conversationMessage
            FROM ConverstaionMessage conversationMessage
            WHERE
               (
                    (conversationMessage.senderUserId = :firstUserId AND conversationMessage.receiverUserId = :secondUserId)
                    OR (conversationMessage.senderUserId = :secondUserId AND conversationMessage.receiverUserId = :firstUserId)
               )
               AND (:messageId IS NULL OR conversationMessage.id < :messageId)
            ORDER BY conversationMessage.id desc
            """)
    List<ConverstaionMessage> findBeforeMessagesBetweenUserIds(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId,
            @Param("messageId") Long messageId,
            Pageable pageable
    );
}
