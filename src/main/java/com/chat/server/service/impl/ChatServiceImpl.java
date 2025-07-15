package com.chat.server.service.impl;

import com.chat.server.domain.entity.Chat;
import com.chat.server.domain.repository.ChatRepository;
import com.chat.server.service.ChatService;
import com.chat.server.service.payload.MessagePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void saveChat(MessagePayload messagePayload) {
        Chat newChat = Chat.of(messagePayload.from(),
                messagePayload.to(),
                messagePayload.message());
        chatRepository.save(newChat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessagePayload> findRecentChats(String firstUsername, String secondUsername, Pageable pageable) {
        return chatRepository.findRecentChatsBetweenUsernames(firstUsername, secondUsername, pageable).stream()
                .map(MessagePayload::of)
                .toList();
    }
}
