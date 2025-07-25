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
            SELECT c FROM Chat c
            WHERE (c.sender = :firstUsername AND c.receiver = :secondUsername)
               OR (c.sender = :secondUsername AND c.receiver = :firstUsername)
            ORDER BY c.tId DESC
            """)
    List<Chat> findRecentChatsBetweenUsernames(
            @Param("firstUsername") String firstUsername,
            @Param("secondUsername") String secondUsername,
            Pageable pageable
    );
}
