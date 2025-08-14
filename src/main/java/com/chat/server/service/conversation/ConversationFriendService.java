package com.chat.server.service.conversation;

import com.chat.server.service.user.response.UserInfoResponse;

import java.util.List;

public interface ConversationFriendService {
    List<UserInfoResponse> findFriends(Long userId);

    void addFriend(Long userId,
                   Long friendUserId);

    void removeFriend(Long userId,
                      Long friendUserId);

    List<UserInfoResponse> findFriendsByKeyword(String keyword,
                                                Long userId);
}
