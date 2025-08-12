package com.chat.server.domain.repository.chat;

import com.chat.server.domain.entity.chat.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("""
            SELECT
               chat
            FROM ChatMessage chat
            WHERE
               (
                    (chat.senderUsername = :firstUsername AND chat.receiverUsername = :secondUsername)
                    OR (chat.senderUsername = :secondUsername AND chat.receiverUsername = :firstUsername)
               )
               AND (:chatId IS NULL OR chat.id < :chatId)
            ORDER BY chat.id desc
            """)
    List<ChatMessage> findBeforeChatsBetweenUsernames(
            @Param("firstUsername") String firstUsername,
            @Param("secondUsername") String secondUsername,
            @Param("chatId") Long chatId,
            Pageable pageable
    );

    @Query("""
            SELECT
               chat
            FROM ChatMessage chat
            WHERE
               (
                    (chat.senderUserId = :firstUserId AND chat.receiverUserId = :secondUserId)
                    OR (chat.senderUserId = :secondUserId AND chat.receiverUserId = :firstUserId)
               )
               AND (:chatId IS NULL OR chat.id < :chatId)
            ORDER BY chat.id desc
            """)
    List<ChatMessage> findBeforeChatsBetweenUserIds(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId,
            @Param("chatId") Long chatId,
            Pageable pageable
    );
}
