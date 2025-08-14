package com.chat.server.service.conversation;

import com.chat.server.service.conversation.request.ConversationCreateRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationInfoResponse> findConversations(Long userId);

    void joinConversationGroup(Long userId,
                               Long conversationId);

    void leaveConversationGroup(Long userId,
                                Long conversationId);

    void createConversation(Long userId,
                            ConversationCreateRequest request);
}
