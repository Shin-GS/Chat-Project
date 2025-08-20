package com.chat.server.service.conversation;

import com.chat.server.service.common.response.PageResponse;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(Long userId);

    PageResponse<ConversationInfoResponse> findConversations(Long userId,
                                                             String keyword,
                                                             Pageable pageable);

    Long joinOneToOne(Long requestUserId,
                      Long targetUserId);

    Long joinGroup(Long userId,
                   Long conversationId);

    void leave(Long userId,
               Long conversationId);

    Long createGroup(Long requestUserId,
                     Set<Long> targetUserIds,
                     String title,
                     String joinCode,
                     boolean hidden);

    ConversationInfoResponse getConversation(Long conversationId,
                                             Long userId);

    List<Long> findParticipantUserIds(Long conversationId);
}
