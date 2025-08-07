package com.chat.server.service.response;

import com.chat.server.domain.dto.UserDto;

public record UserInfoResponse(Long id,
                               String name) {
    public static UserInfoResponse of(UserDto userDto) {
        return new UserInfoResponse(userDto.id(), userDto.name());
    }
}
