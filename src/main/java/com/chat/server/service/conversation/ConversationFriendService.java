package com.chat.server.service.conversation;

import com.chat.server.domain.vo.UserId;
import com.chat.server.service.user.response.UserInfoResponse;

import java.util.List;

public interface ConversationFriendService {
    List<UserInfoResponse> findFriends(UserId userId);

    void addFriend(UserId userId,
                   UserId friendUserId);

    void removeFriend(UserId userId,
                      UserId friendUserId);

    List<UserInfoResponse> findFriendsByKeyword(String keyword,
                                                UserId userId);
}
