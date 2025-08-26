package com.chat.server.event;

import com.chat.server.common.constant.Constants;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.repository.conversation.message.ConversationMessageRepository;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemMessageEventHandler {
    private final ConversationService conversationService;
    private final ConversationMessageRepository conversationMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpringTemplateEngine templateEngine;

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
        Map<String, Object> headers = Map.of(
                "content-type", "text/html; charset=UTF-8",
                Constants.USER_UI_REFRESH_IDS,
                event.refreshIds() == null || event.refreshIds().isEmpty() ? "" : String.join(",", event.refreshIds())
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

    private String renderChatMessageFragment(ConversationMessageResponse conversationMessageResponse) {
        Context context = new Context();
        context.setVariable("messages", List.of(conversationMessageResponse));
        return templateEngine.process("components/conversation/message/list", context);
    }
}
