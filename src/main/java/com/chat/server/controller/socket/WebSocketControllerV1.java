package com.chat.server.controller.socket;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationOneToOneService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketControllerV1 {
    private final ConversationMessageService conversationMessageService;
    private final ConversationService conversationService;
    private final ConversationOneToOneService conversationOneToOneService;

    @MessageMapping("/conversations/message")
    public void receivedMessage(ConversationMessageRequest message,
                                Principal principal) {
        UserId userId = UserId.of(Long.parseLong(principal.getName()));
        ConversationId conversationId = message.conversationId();

        reJoinOneToOneConversation(conversationId, userId);
        Long conversationMessageId = conversationMessageService.saveMessage(userId, message.conversationId(), message.message());
        log.info("Message saved -> From: {}, to: {}, msg: {}", userId, conversationId, conversationMessageId);
    }

    private void reJoinOneToOneConversation(ConversationId conversationId,
                                            UserId userId) {
        ConversationInfoResponse conversation = conversationService.getAccessibleConversation(conversationId, userId);
        if (conversation.type() != ConversationType.ONE_TO_ONE) {
            return;
        }

        UserId otherUserId = conversationOneToOneService.getOtherUserId(userId, conversationId);
        if (conversationOneToOneService.isUserLeft(otherUserId, conversationId)) {
            conversationOneToOneService.join(userId, otherUserId);
        }
    }
}
