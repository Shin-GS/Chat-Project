package com.chat.server.service.conversation.response;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.dto.ConversationParticipantDto;
import com.chat.server.domain.vo.UserId;

public record ConversationParticipantInfoResponse(UserId userId,
                                                  String name,
                                                  ConversationUserRole role) {
    public static ConversationParticipantInfoResponse of(ConversationParticipantDto dto) {
        return new ConversationParticipantInfoResponse(dto.userId(), dto.name(), dto.role());
    }
}
