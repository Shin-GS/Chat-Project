package com.chat.server.domain.dto;

import com.chat.server.common.constant.conversation.ConversationType;

import java.time.LocalDateTime;

public record ConversationDto(Long id,
                              ConversationType type,
                              String title,
                              LocalDateTime lastActivityAt) {
}
