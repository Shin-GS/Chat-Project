package com.chat.server.service.conversation;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.response.ConversationInfoAndMessageResponse;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationParticipantInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConversationService {
    List<ConversationInfoAndMessageResponse> findConversations(UserId userId);

    ConversationInfoAndMessageResponse findConversation(UserId userId, ConversationId conversationId);

    ConversationInfoResponse getAccessibleConversation(ConversationId conversationId,
                                                       UserId userId);

    List<UserId> findParticipantUserIds(ConversationId conversationId);

    List<ConversationParticipantInfoResponse> findParticipants(ConversationId conversationId,
                                                               UserId userId);

    void leave(UserId userId,
               ConversationId conversationId);

    String uploadImage(UserId userId,
                       MultipartFile file);
}
