package com.chat.server.service.conversation;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.response.ConversationInfoResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(UserId userId);

    ConversationInfoResponse getAccessibleConversation(ConversationId conversationId,
                                                       UserId userId);

    List<UserId> findParticipantUserIds(ConversationId conversationId);

    void leave(UserId userId,
               ConversationId conversationId);
}
