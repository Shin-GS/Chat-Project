package com.chat.server.service.response;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.User;
import com.chat.server.service.security.JwtMemberInfo;

public record UserInfoResponse(Long id,
                               String name,
                               boolean friend) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(userDto.id(), userDto.name(), userDto.friend());
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(user.getId(), user.getUsername(), Boolean.FALSE);
    }

    public static UserInfoResponse of(JwtMemberInfo userInfo) {
        return new UserInfoResponse(userInfo.id(), userInfo.username(), Boolean.FALSE);
    }
}
