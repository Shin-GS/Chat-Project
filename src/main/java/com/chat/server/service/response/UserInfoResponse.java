package com.chat.server.service.response;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.User;

public record UserInfoResponse(Long id,
                               String name,
                               boolean friend) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(userDto.id(), userDto.name(), userDto.friend());
    }

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(user.getId(), user.getUsername(), Boolean.FALSE);
    }
}
