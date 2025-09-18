package com.chat.server.event;

import com.chat.server.common.constant.Constants;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.repository.conversation.message.ConversationMessageRepository;
import com.chat.server.event.message.ConversationMessageEvent;
import com.chat.server.event.message.RefreshConversationUiEvent;
import com.chat.server.event.message.SystemMessageEvent;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageEventHandler {
    private final ConversationService conversationService;
    private final ConversationMessageRepository conversationMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpringTemplateEngine templateEngine;
    private final ConversationMessageService conversationMessageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSystemMessageEvent(SystemMessageEvent event) {
        if (event == null || event.conversationId() == null || event.messageId() == null || event.socketDestination() == null) {
            log.error("invalid event: SystemMessageEvent: event is empty");
            return;
        }

        Optional<ConversationMessage> optionalMessage = conversationMessageRepository.findById(event.messageId());
        if (optionalMessage.isEmpty()) {
            return;
        }

        String senderHtml = renderChatMessageFragment(ConversationMessageResponse.ofSystem(optionalMessage.get()));
        List<String> refreshIds = ObjectUtils.isEmpty(event.refreshIds()) ? new ArrayList<>() : new ArrayList<>(event.refreshIds());
        refreshIds.add("conversation-" + event.conversationId() + "-read");
        Map<String, Object> headers = Map.of(
                "content-type", "text/html; charset=UTF-8",
                Constants.USER_UI_REFRESH_IDS,
                String.join(",", refreshIds)
        );
        conversationService.findParticipantUserIds(event.conversationId())
                .forEach(participantUserId ->
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(participantUserId),
                                event.socketDestination(),
                                senderHtml,
                                headers)
                );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleConversationMessageEvent(ConversationMessageEvent event) {
        if (event == null || event.conversationId() == null || event.fromUserId() == null || event.messageId() == null) {
            log.error("invalid event: ConversationMessageEvent: event is empty");
            return;
        }

        Optional<ConversationMessage> optionalMessage = conversationMessageRepository.findById(event.messageId());
        if (optionalMessage.isEmpty()) {
            return;
        }

        ConversationMessage conversationMessage = optionalMessage.get();
        String senderHtml = renderChatMessageFragment(conversationMessageService.convertMessageResponse(conversationMessage, event.fromUserId()));
        String receiverHtml = renderChatMessageFragment(conversationMessageService.convertMessageResponse(conversationMessage, null));
        List<String> refreshIds = List.of("refresh-conversation-list", "conversation-" + conversationMessage.getConversationId() + "-read");
        Map<String, Object> headers = Map.of(
                "content-type", "text/html; charset=UTF-8",
                Constants.USER_UI_REFRESH_IDS,
                String.join(",", refreshIds)
        );

        conversationService.findParticipantUserIds(conversationMessage.getConversationId())
                .forEach(participantUserId ->
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(participantUserId),
                                Constants.SOCKET_DESTINATION_CONVERSATION_MESSAGE.formatted(conversationMessage.getConversationId()),
                                participantUserId.equals(event.fromUserId()) ? senderHtml : receiverHtml,
                                headers)
                );
    }

    private String renderChatMessageFragment(ConversationMessageResponse conversationMessageResponse) {
        Context context = new Context();
        context.setVariable("messages", List.of(conversationMessageResponse));
        return templateEngine.process("components/conversation/message/list", context);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRefreshConversationUiEvent(RefreshConversationUiEvent event) {
        if (event == null || event.conversationId() == null) {
            log.error("invalid event: RefreshConversationUiEvent: event is empty");
            return;
        }

        List<String> refreshIds = List.of("refresh-conversation-list");
        Map<String, Object> headers = Map.of(
                "content-type", "text/html; charset=UTF-8",
                Constants.USER_UI_REFRESH_IDS,
                String.join(",", refreshIds)
        );

        conversationService.findParticipantUserIds(event.conversationId())
                .forEach(participantUserId ->
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(participantUserId),
                                Constants.SOCKET_DESTINATION_CONVERSATION_USER_QUEUE_UI,
                                "",
                                headers)
                );
    }
}
