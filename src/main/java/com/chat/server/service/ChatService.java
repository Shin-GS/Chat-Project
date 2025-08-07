package com.chat.server.service;

import com.chat.server.service.payload.MessagePayload;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    void saveChat(MessagePayload messagePayload);

    List<MessagePayload> findRecentChats(String firstUsername, String secondUsername, Pageable pageable);

    void addFriend(Long userId, Long friendId);
}
