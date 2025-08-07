package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.Chat;
import com.chat.server.domain.entity.ChatFriend;
import com.chat.server.domain.repository.ChatFriendRepository;
import com.chat.server.domain.repository.ChatRepository;
import com.chat.server.service.ChatService;
import com.chat.server.service.payload.MessagePayload;
import com.chat.server.service.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatFriendRepository chatFriendRepository;

    @Override
    @Transactional
    public void saveChat(MessagePayload messagePayload) {
        Chat newChat = Chat.of(messagePayload.from(),
                messagePayload.to(),
                messagePayload.message());
        chatRepository.save(newChat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessagePayload> findRecentChats(String firstUsername, String secondUsername, Pageable pageable) {
        return chatRepository.findRecentChatsBetweenUsernames(firstUsername, secondUsername, pageable).stream()
                .map(MessagePayload::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoResponse> findFriends(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        return chatFriendRepository.findAllByUserIdOrderByName(userId).stream()
                .map(UserInfoResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public void addFriend(Long userId, Long friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        if (chatFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_FRIEND_ALREADY_EXISTS);
        }

        chatFriendRepository.save(ChatFriend.of(userId, friendUserId));
    }

    @Override
    @Transactional
    public void removeFriend(Long userId, Long friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        if (!chatFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_FRIEND_NOT_EXISTS);
        }

        chatFriendRepository.deleteByUserIdAndFriendUserId(userId, friendUserId);
    }
}
