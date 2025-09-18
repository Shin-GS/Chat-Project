package com.chat.server.service.conversation;

import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationMessageService {
    void handleMessage(UserId userId,
                       ConversationId conversationId,
                       ConversationMessageType type,
                       String message,
                       Long stickerId);

    void handleSystemMessage(UserId userId,
                             ConversationId conversationId,
                             String message,
                             List<String> refreshIds);

    List<ConversationMessageResponse> findBeforeMessage(UserId userId,
                                                        ConversationId conversationId,
                                                        Long messageId,
                                                        Pageable pageable);

    Long findRecentlyMessageId(ConversationId conversationId);

    void readMessage(UserId userId,
                     ConversationId conversationId);

    ConversationMessageResponse convertMessageResponse(ConversationMessage message,
                                                       UserId userId);
}
