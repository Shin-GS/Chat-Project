package com.chat.server.service.conversation;

import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ConversationGroupService {
    Long create(Long requestUserId,
                Set<Long> targetUserIds,
                String title,
                String joinCode,
                boolean hidden);

    Long join(Long userId,
              Long conversationId);

    void leave(Long userId,
               Long conversationId);

    CustomPageResponse<ConversationInfoResponse> findConversations(Long userId,
                                                                   String keyword,
                                                                   Pageable pageable);
}
