package com.chat.server.service.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.chat.Chat;
import com.chat.server.domain.entity.chat.ChatFriend;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.ChatFriendRepository;
import com.chat.server.domain.repository.ChatRepository;
import com.chat.server.domain.repository.UserRepository;
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
    private final ChatRepository chatRepository;
    private final ChatFriendRepository chatFriendRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponse saveChat(Long userId,
                                        ChatMessageRequest messageRequest) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        User receiver = userRepository.findById(messageRequest.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Chat chatMessage = chatRepository.save(Chat.of(sender, receiver, messageRequest.message()));
        return ChatMessageResponse.of(chatMessage, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findBeforeChats(Long userId,
                                                     String firstUsername,
                                                     String secondUsername,
                                                     Long chatId,
                                                     Pageable pageable) {
        return chatRepository.findBeforeChatsBetweenUsernames(firstUsername, secondUsername, chatId, pageable).stream()
                .sorted(Comparator.comparing(Chat::getTId))
                .map(chat -> ChatMessageResponse.of(chat, userId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findBeforeChats(Long userId,
                                                     Long friendUserId,
                                                     Long chatId,
                                                     Pageable pageable) {
        return chatRepository.findBeforeChatsBetweenUserIds(userId, friendUserId, chatId, pageable).stream()
                .sorted(Comparator.comparing(Chat::getTId))
                .map(chat -> ChatMessageResponse.of(chat, userId))
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
    public void addFriend(Long userId,
                          Long friendUserId) {
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
    public void removeFriend(Long userId,
                             Long friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_REQUEST_INVALID);
        }

        if (!chatFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CHAT_FRIEND_NOT_EXISTS);
        }

        chatFriendRepository.deleteByUserIdAndFriendUserId(userId, friendUserId);
    }
}
