package com.chat.server.service;

import com.chat.server.model.payload.MessagePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    public void saveChatMessage(MessagePayload messagePayload) {
        // todo 메시지 db 저장
    }
}
