package com.chat.server.service;

import com.chat.server.service.payload.MessagePayload;
import com.chat.server.service.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    void saveChat(MessagePayload messagePayload);

    List<MessagePayload> findRecentChats(String firstUsername, String secondUsername, Pageable pageable);

    List<UserInfoResponse> findFriends(Long userId);

    void addFriend(Long userId, Long friendUserId);
}
