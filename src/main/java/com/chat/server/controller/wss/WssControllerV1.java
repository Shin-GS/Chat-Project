package com.chat.server.controller.wss;

import com.chat.server.service.ConversationService;
import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.security.JwtMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WssControllerV1 {
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpringTemplateEngine templateEngine;

    @MessageMapping("/chat/message")
    public void receivedMessage(ChatMessageRequest message,
                                Principal principal) {
        JwtMemberInfo memberInfo = (JwtMemberInfo) ((Authentication) principal).getPrincipal();
        Long senderId = memberInfo.id();
        Long receiverId = message.userId();
        log.info("Message received -> From: {}, to: {}, msg: {}", senderId, receiverId, message.message());

        ChatMessageResponse senderResponse = conversationService.saveChat(senderId, message);
        messagingTemplate.convertAndSend("/sub/chat/" + senderId, renderChatMessageFragment(senderResponse));

        ChatMessageResponse receiverResponse = ChatMessageResponse.of(senderResponse.id(), senderResponse.from(), senderResponse.to(), senderResponse.message());
        messagingTemplate.convertAndSend("/sub/chat/" + receiverId, renderChatMessageFragment(receiverResponse));
    }

    private String renderChatMessageFragment(ChatMessageResponse chatMessageResponse) {
        Context context = new Context();
        context.setVariable("messages", List.of(chatMessageResponse));
        return templateEngine.process("components/chat/chat/list", context);
    }
}
