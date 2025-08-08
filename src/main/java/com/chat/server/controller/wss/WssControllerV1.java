package com.chat.server.controller.wss;

import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.ChatService;
import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WssControllerV1 {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void receivedMessage(ChatMessageRequest message, Authentication authentication) {
        JwtMemberInfo memberInfo = (JwtMemberInfo) authentication.getPrincipal();
        log.info("Message received -> From: {}, to: {}, msg: {}", memberInfo.id(), message.userId(), message.message());

        ChatMessageResponse chatMessage = chatService.saveChat(memberInfo.id(), message);
        messagingTemplate.convertAndSend("/sub/chat/" + message.userId(), chatMessage); // 수신자에게 전송
        messagingTemplate.convertAndSend("/sub/chat/" + memberInfo.id(), chatMessage); // 본인에게 echo 전송
    }
}
