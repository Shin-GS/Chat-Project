package com.chat.server.domain.repository;

import com.chat.server.domain.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("""
            SELECT
               c
            FROM Chat c
            WHERE
               (
                    (c.sender = :firstUsername AND c.receiver = :secondUsername)
                    OR (c.sender = :secondUsername AND c.receiver = :firstUsername)
               )
               AND (:chatId IS NULL OR c.tId < :chatId)
            ORDER BY c.tId desc
            """)
    List<Chat> findBeforeChatsBetweenUsernames(
            @Param("firstUsername") String firstUsername,
            @Param("secondUsername") String secondUsername,
            @Param("chatId") Long chatId,
            Pageable pageable
    );

    @Query("""
            SELECT
               c
            FROM Chat c
            WHERE
               (
                    (c.senderUserId = :firstUserId AND c.receiverUserId = :secondUserId)
                    OR (c.senderUserId = :secondUserId AND c.receiverUserId = :firstUserId)
               )
               AND (:chatId IS NULL OR c.tId < :chatId)
            ORDER BY c.tId desc
            """)
    List<Chat> findBeforeChatsBetweenUserIds(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId,
            @Param("chatId") Long chatId,
            Pageable pageable
    );
}
