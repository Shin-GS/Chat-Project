package com.chat.server.service.conversation.request;

import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.domain.vo.ConversationId;

public record ConversationMessageRequest(ConversationId conversationId,
                                         ConversationMessageType type,
                                         String message,
                                         Long stickerId) {
}
