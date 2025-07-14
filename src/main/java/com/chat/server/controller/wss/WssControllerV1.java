package com.chat.server.controller.wss;

import com.chat.server.model.payload.MessagePayload;
import com.chat.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WssControllerV1 {
    private final ChatService chatService;

    @MessageMapping("/chat/message/{from}")
    @SendTo("/sub/chat")
    public MessagePayload receivedMessage(@DestinationVariable String from,
                                          MessagePayload messagePayload) {
        log.info("Message received -> From: {}, to: {}, msg: {}", from, messagePayload.to(), messagePayload.message());
        chatService.saveChatMessage(messagePayload);
        return messagePayload;
    }
}
