package com.chat.server.domain.dto;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.vo.UserId;

public record ConversationParticipantDto(UserId userId,
                                         String name,
                                         ConversationUserRole role) {
}
