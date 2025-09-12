package com.chat.server.service.user.response;

import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.UserId;

public record UserProfileResponse(UserId id,
                                  String accountId,
                                  String name,
                                  String profileImageUrl,
                                  String statusMessage) {
    public static UserProfileResponse of(User user) {
        return new UserProfileResponse(
                UserId.of(user.getId()),
                user.getAccountId(),
                user.getUsername(),
                user.getProfileImageUrl(),
                user.getStatusMessage());
    }
}
