package com.chat.server.domain.dto;

public record UserDto(Long id,
                      String accountId,
                      String name,
                      boolean friend) {
}
