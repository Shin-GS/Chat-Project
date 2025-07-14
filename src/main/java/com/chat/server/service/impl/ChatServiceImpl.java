package com.chat.server.service.impl;

import com.chat.server.domain.entity.Chat;
import com.chat.server.domain.repository.ChatRepository;
import com.chat.server.model.payload.MessagePayload;
import com.chat.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Transactional
    public void saveChatMessage(MessagePayload messagePayload) {
        Chat newChat = Chat.of(messagePayload.from(),
                messagePayload.to(),
                messagePayload.message());
        chatRepository.save(newChat);
    }
}
