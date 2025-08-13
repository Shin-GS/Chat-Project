package com.chat.server.service;

import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.request.conversation.ConversationCreateRequest;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.response.ConversationInfoResponse;
import com.chat.server.service.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationService {
    ChatMessageResponse saveChat(Long userId,
                                 ChatMessageRequest messageRequest);

    List<ChatMessageResponse> findBeforeChats(Long userId,
                                              String firstUsername,
                                              String secondUsername,
                                              Long chatId,
                                              Pageable pageable);

    List<ChatMessageResponse> findBeforeChats(Long userId,
                                              Long friendUserId,
                                              Long chatId,
                                              Pageable pageable);

    List<UserInfoResponse> findFriends(Long userId);

    void addFriend(Long userId,
                   Long friendUserId);

    void removeFriend(Long userId,
                      Long friendUserId);

    List<ConversationInfoResponse> findConversations(Long userId);

    void joinConversationGroup(Long userId,
                               Long conversationId);

    void leaveConversationGroup(Long userId,
                                Long conversationId);

    void createConversation(Long userId,
                            ConversationCreateRequest request);
}
