package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationInfoResponse> findConversations(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return conversationRepository.findAllByUserIdOrderLastActivityAt(userId).stream()
                .map(ConversationInfoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationInfoResponse getConversation(Long conversationId,
                                                    Long userId) {
        return conversationRepository.findConversationDtoById(conversationId, userId)
                .map(ConversationInfoResponse::of)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findParticipantUserIds(Long conversationId) {
        return conversationParticipantRepository.findAllByConversationId(conversationId).stream()
                .map(ConversationParticipant::getUserId)
                .toList();
    }
}
