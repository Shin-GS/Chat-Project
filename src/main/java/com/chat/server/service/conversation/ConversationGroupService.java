package com.chat.server.service.conversation;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ConversationGroupService {
    ConversationId create(UserId requestUserId,
                          Set<UserId> targetUserIds,
                          String title,
                          String joinCode,
                          boolean hidden);

    ConversationId join(UserId userId,
                        String joinCode,
                        ConversationId conversationId);

    void leave(UserId userId,
               ConversationId conversationId);

    CustomPageResponse<ConversationInfoResponse> findConversations(UserId userId,
                                                                   String keyword,
                                                                   Pageable pageable);

    ConversationInfoResponse getJoinAbleConversation(ConversationId conversationId,
                                                     UserId userId);

    String getGroupTitle(ConversationId conversationId);

    void changeRole(UserId requestUserId,
                    ConversationId conversationId,
                    UserId targetUserId,
                    ConversationUserRole role);

    ConversationUserRole getRole(ConversationId conversationId,
                                 UserId userId);
}
