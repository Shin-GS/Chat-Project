package com.chat.server.domain.repository.conversation.query;

import com.chat.server.domain.dto.ConversationParticipantDto;
import com.chat.server.domain.vo.ConversationId;

import java.util.List;

public interface ConversationParticipantQueryRepository {
    List<ConversationParticipantDto> findDtoAllByConversationId(ConversationId conversationId);
}
