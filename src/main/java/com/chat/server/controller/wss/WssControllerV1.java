package com.chat.server.controller.wss;

import com.chat.server.service.ChatService;
import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.security.JwtMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WssControllerV1 {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void receivedMessage(ChatMessageRequest message,
                                Principal principal) {
        JwtMemberInfo memberInfo = (JwtMemberInfo) ((Authentication) principal).getPrincipal();
        log.info("Message received -> From: {}, to: {}, msg: {}", memberInfo.id(), message.userId(), message.message());

        ChatMessageResponse chatMessage = chatService.saveChat(memberInfo.id(), message);
        messagingTemplate.convertAndSend("/sub/chat/" + message.userId(), chatMessage);
        messagingTemplate.convertAndSend("/sub/chat/" + memberInfo.id(), chatMessage);
    }
}
