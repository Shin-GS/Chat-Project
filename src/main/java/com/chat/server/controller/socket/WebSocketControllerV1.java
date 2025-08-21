package com.chat.server.controller.socket;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationOneToOneService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpringTemplateEngine templateEngine;
    private final ConversationOneToOneService conversationOneToOneService;

    @MessageMapping("/conversations/message")
    public void receivedMessage(ConversationMessageRequest message,
                                Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Long conversationId = message.conversationId();
        log.info("Message received -> From: {}, to: {}, msg: {}", userId, conversationId, message.message());

        reJoinOneToOneConversation(conversationId, userId);

        ConversationMessage conversationMessage = conversationMessageService.saveMessage(userId, message.conversationId(), message.message());
        String senderHtml = renderChatMessageFragment(ConversationMessageResponse.ofSender(conversationMessage));
        String receiverHtml = renderChatMessageFragment(ConversationMessageResponse.ofReceiver(conversationMessage));
        conversationService.findParticipantUserIds(message.conversationId())
                .forEach(participantUserId ->
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(participantUserId),
                                "/sub/conversations/" + message.conversationId(),
                                participantUserId.equals(userId) ? senderHtml : receiverHtml)
                );
    }

    private void reJoinOneToOneConversation(Long conversationId,
                                            Long userId) {
        ConversationInfoResponse conversation = conversationService.getConversation(conversationId, userId);
        if (!conversation.type().equals(ConversationType.ONE_TO_ONE)) {
            return;
        }

        Long otherUserId = conversationOneToOneService.getOtherUserId(userId, conversationId);
        if (conversationOneToOneService.isUserLeft(otherUserId, conversationId)) {
            conversationOneToOneService.join(userId, otherUserId);
        }
    }

    private String renderChatMessageFragment(ConversationMessageResponse conversationMessageResponse) {
        Context context = new Context();
        context.setVariable("messages", List.of(conversationMessageResponse));
        return templateEngine.process("components/conversation/message/list", context);
    }
}
