package com.chat.server.socket.handler;

import com.chat.server.socket.handler.payload.MessagePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WssHandler extends TextWebSocketHandler { // 현 프로젝트에서 사용 X
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            MessagePayload messagePayload = objectMapper.readValue(payload, MessagePayload.class);
            log.info("messagePayload: {}", messagePayload);

            session.sendMessage(new TextMessage(payload));

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        super.handleTextMessage(session, message);
    }
}
