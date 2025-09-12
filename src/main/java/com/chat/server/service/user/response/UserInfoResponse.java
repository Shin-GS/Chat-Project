package com.chat.server.service.user.response;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.UserId;

public record UserInfoResponse(UserId id,
                               String accountId,
                               String name,
                               String profileImageUrl,
                               String statusMessage,
                               boolean friend) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(UserId.of(userDto.id()),
                userDto.accountId(),
                userDto.name(),
                userDto.profileImageUrl(),
                userDto.statusMessage(),
                userDto.friend());
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(UserId.of(user.getId()),
                user.getAccountId(),
                user.getUsername(),
                user.getProfileImageUrl(),
                user.getStatusMessage(),
                Boolean.FALSE);
    }
}
