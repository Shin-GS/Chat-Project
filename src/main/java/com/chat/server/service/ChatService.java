package com.chat.server.service;

import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    ChatMessageResponse saveChat(Long userId, ChatMessageRequest messageRequest);

    List<ChatMessageResponse> findRecentChats(Long userId, String firstUsername, String secondUsername, Pageable pageable);

    List<ChatMessageResponse> findRecentChats(Long userId, Long friendUserId, Pageable pageable);

    List<UserInfoResponse> findFriends(Long userId);

    void addFriend(Long userId, Long friendUserId);

    void removeFriend(Long userId, Long friendUserId);
}
