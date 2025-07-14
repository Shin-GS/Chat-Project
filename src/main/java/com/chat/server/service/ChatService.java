package com.chat.server.service;

import com.chat.server.model.payload.MessagePayload;

public interface ChatService {
    void saveChatMessage(MessagePayload messagePayload);
}
