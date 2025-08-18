package com.chat.server.controller.socket;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
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
public class WebSocketControllerV1 {
    private final ConversationMessageService conversationMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpringTemplateEngine templateEngine;

    @MessageMapping("/conversations/message")
    public void receivedMessage(ConversationMessageRequest message,
                                Principal principal) {
        JwtMemberInfo memberInfo = (JwtMemberInfo) ((Authentication) principal).getPrincipal();
        log.info("Message received -> From: {}, to: {}, msg: {}", memberInfo.id(), message.conversationId(), message.message());

        ConversationMessage conversationMessage = conversationMessageService.saveMessage(memberInfo.id(), message.conversationId(), message.message());
        messagingTemplate.convertAndSend("/sub/conversations/" + message.conversationId(),
                renderChatMessageFragment(ConversationMessageResponse.ofSender(conversationMessage)));
        messagingTemplate.convertAndSend("/sub/conversations/" + message.conversationId(),
                renderChatMessageFragment(ConversationMessageResponse.ofReceiver(conversationMessage)));
    }

    private String renderChatMessageFragment(ConversationMessageResponse conversationMessageResponse) {
        Context context = new Context();
        context.setVariable("messages", List.of(conversationMessageResponse));
        return templateEngine.process("components/conversation/message/list", context);
    }
}
