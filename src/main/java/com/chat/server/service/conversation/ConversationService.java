package com.chat.server.service.conversation;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationParticipantInfoResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(UserId userId);

    ConversationInfoResponse getAccessibleConversation(ConversationId conversationId,
                                                       UserId userId);

    List<UserId> findParticipantUserIds(ConversationId conversationId);

    List<ConversationParticipantInfoResponse> findParticipants(ConversationId conversationId,
                                                               UserId userId);

    void leave(UserId userId,
               ConversationId conversationId);
}
