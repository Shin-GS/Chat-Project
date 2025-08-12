package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.chat.ChatMessage;
import com.chat.server.domain.entity.user.UserFriend;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.user.UserFriendRepository;
import com.chat.server.domain.repository.chat.ChatMessageRepository;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.service.ChatService;
import com.chat.server.service.request.ChatMessageRequest;
import com.chat.server.service.response.ChatMessageResponse;
import com.chat.server.service.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserFriendRepository userFriendRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponse saveChat(Long userId,
                                        ChatMessageRequest messageRequest) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        User receiver = userRepository.findById(messageRequest.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.of(sender, receiver, messageRequest.message()));
        return ChatMessageResponse.of(chatMessage, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findBeforeChats(Long userId,
                                                     String firstUsername,
                                                     String secondUsername,
                                                     Long chatId,
                                                     Pageable pageable) {
        return chatMessageRepository.findBeforeChatsBetweenUsernames(firstUsername, secondUsername, chatId, pageable).stream()
                .sorted(Comparator.comparing(ChatMessage::getId))
                .map(chat -> ChatMessageResponse.of(chat, userId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findBeforeChats(Long userId,
                                                     Long friendUserId,
                                                     Long chatId,
                                                     Pageable pageable) {
        return chatMessageRepository.findBeforeChatsBetweenUserIds(userId, friendUserId, chatId, pageable).stream()
                .sorted(Comparator.comparing(ChatMessage::getId))
                .map(chat -> ChatMessageResponse.of(chat, userId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoResponse> findFriends(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        return userFriendRepository.findAllByUserIdOrderByName(userId).stream()
                .map(UserInfoResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public void addFriend(Long userId,
                          Long friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        if (userFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_FRIEND_ALREADY_EXISTS);
        }

        userFriendRepository.save(UserFriend.of(userId, friendUserId));
    }

    @Override
    @Transactional
    public void removeFriend(Long userId,
                             Long friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        if (!userFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_FRIEND_NOT_EXISTS);
        }

        userFriendRepository.deleteByUserIdAndFriendUserId(userId, friendUserId);
    }
}
