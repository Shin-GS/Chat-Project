package com.chat.server.service.conversation;

import com.chat.server.service.conversation.request.ConversationCreateRequest;
import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationService {
    ConversationMessageResponse saveMessage(Long userId,
                                            ConversationMessageRequest messageRequest);

    List<ConversationMessageResponse> findBeforeMessage(Long userId,
                                                        Long friendUserId,
                                                        Long messageId,
                                                        Pageable pageable);

    List<ConversationInfoResponse> findConversations(Long userId);

    void joinConversationGroup(Long userId,
                               Long conversationId);

    void leaveConversationGroup(Long userId,
                                Long conversationId);

    void createConversation(Long userId,
                            ConversationCreateRequest request);
}
