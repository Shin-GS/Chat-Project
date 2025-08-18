package com.chat.server.service.conversation;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.service.conversation.response.ConversationInfoResponse;

import java.util.List;
import java.util.Set;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(Long userId);

    Long joinOneToOne(Long userId,
                      Long friendUserId);

    Long joinGroup(Long userId,
                   Long conversationId);

    void leave(Long userId,
               Long conversationId);

    Long create(Long userId,
                ConversationType type,
                Set<Long> userIds,
                String title);

    ConversationInfoResponse getConversation(Long conversationId,
                                             Long userId);

    List<Long> findParticipantUserIds(Long conversationId);
}
