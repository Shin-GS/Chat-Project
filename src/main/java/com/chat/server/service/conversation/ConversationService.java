package com.chat.server.service.conversation;

import com.chat.server.service.conversation.response.ConversationInfoResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(Long userId);

    ConversationInfoResponse getConversation(Long conversationId,
                                             Long userId);

    List<Long> findParticipantUserIds(Long conversationId);
}
