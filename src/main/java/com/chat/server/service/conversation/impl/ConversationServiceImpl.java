package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationOneToOneKeyRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationOneToOneKeyRepository conversationOneToOneKeyRepository;

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
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));

        // oneToOne conversation
        if (conversation.getType().equals(ConversationType.ONE_TO_ONE)) {
            String oneToOneTitle = conversationOneToOneKeyRepository.findOtherUsername(conversationId, userId)
                    .orElse("Deleted user");
            return ConversationInfoResponse.of(conversation, oneToOneTitle);
        }

        // group conversation
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        String groupTitle = StringUtils.isBlank(conversation.getTitle()) ? "Untitled group" : conversation.getTitle();
        return ConversationInfoResponse.of(conversation, groupTitle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findParticipantUserIds(Long conversationId) {
        return conversationParticipantRepository.findAllByConversationId(conversationId).stream()
                .map(ConversationParticipant::getUserId)
                .toList();
    }
}
