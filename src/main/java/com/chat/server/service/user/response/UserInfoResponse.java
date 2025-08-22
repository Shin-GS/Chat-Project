package com.chat.server.service.user.response;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.security.JwtMemberInfo;

public record UserInfoResponse(UserId id,
                               String accountId,
                               String name,
                               boolean friend) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(UserId.of(userDto.id()), userDto.accountId(), userDto.name(), userDto.friend());
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(UserId.of(user.getId()), user.getAccountId(), user.getUsername(), Boolean.FALSE);
    }

    public static UserInfoResponse of(JwtMemberInfo userInfo) {
        return new UserInfoResponse(userInfo.id(), userInfo.accountId(), userInfo.username(), Boolean.FALSE);
    }
}
