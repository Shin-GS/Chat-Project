package com.chat.server.domain.repository;

import com.chat.server.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findTop10BySenderOrReceiverOrderByTIdDesc(String sender, String receiver);
}

