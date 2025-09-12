package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.user.UserFriend;
import com.chat.server.domain.repository.user.UserFriendRepository;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationFriendService;
import com.chat.server.service.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationFriendServiceImpl implements ConversationFriendService {
    private final UserFriendRepository userFriendRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoResponse> findFriends(UserId userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return userFriendRepository.findAllByUserIdOrderByName(userId).stream()
                .map(UserInfoResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public void addFriend(UserId userId,
                          UserId friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        if (userFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_FRIEND_ALREADY_EXISTS);
        }

        userFriendRepository.save(UserFriend.of(userId, friendUserId));
    }

    @Override
    @Transactional
    public void removeFriend(UserId userId,
                             UserId friendUserId) {
        if (userId == null || friendUserId == null || userId.equals(friendUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        if (!userFriendRepository.existsByUserIdAndFriendUserId(userId, friendUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_FRIEND_NOT_EXISTS);
        }

        userFriendRepository.deleteByUserIdAndFriendUserId(userId, friendUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoResponse> findFriendsByKeyword(String keyword,
                                                       UserId userId) {
        if (keyword == null || keyword.isEmpty() || userId == null) {
            return new ArrayList<>();
        }

        return userFriendRepository.findSimilarNamesExcludingExactMatch(keyword, userId).stream()
                .map(UserInfoResponse::of)
                .toList();
    }
}
