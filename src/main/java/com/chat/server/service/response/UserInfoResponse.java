package com.chat.server.service.response;

import com.chat.server.domain.dto.UserDto;

public record UserInfoResponse(Long id,
                               String name,
                               boolean friend) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(userDto.id(), userDto.name(), userDto.friend());
    }
}
