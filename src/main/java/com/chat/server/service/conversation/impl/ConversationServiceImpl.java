package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationOneToOneKeyRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationOneToOneService;
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
    private final ConversationGroupService conversationGroupService;
    private final ConversationOneToOneService conversationOneToOneService;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationInfoResponse> findConversations(UserId userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return conversationRepository.findAllByUserIdOrderLastActivityAt(userId).stream()
                .map(conversation -> {
                    if (conversation.getType().equals(ConversationType.ONE_TO_ONE)) {
                        return ConversationInfoResponse.of(conversation, getOneToOneTitle(conversation.getId(), userId));
                    }

                    return ConversationInfoResponse.of(conversation, getGroupTitle(conversation));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationInfoResponse getConversation(Long conversationId,
                                                    UserId userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));

        // oneToOne conversation
        if (conversation.getType().equals(ConversationType.ONE_TO_ONE)) {
            return ConversationInfoResponse.of(conversation, getOneToOneTitle(conversationId, userId));
        }

        // group conversation
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return ConversationInfoResponse.of(conversation, getGroupTitle(conversation));
    }

    private String getOneToOneTitle(Long conversationId,
                                    UserId userId) {
        return conversationOneToOneKeyRepository.findOtherUsername(conversationId, userId)
                .orElse("Deleted user");
    }

    private String getGroupTitle(Conversation conversation) {
        return StringUtils.isBlank(conversation.getTitle()) ? "Untitled group" : conversation.getTitle();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserId> findParticipantUserIds(Long conversationId) {
        return conversationParticipantRepository.findAllByConversationId(conversationId).stream()
                .map(ConversationParticipant::getUserId)
                .toList();
    }

    @Override
    public void leave(UserId userId,
                      Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (conversation.getType().equals(ConversationType.ONE_TO_ONE)) {
            conversationOneToOneService.leave(userId, conversationId);
            return;
        }

        conversationGroupService.leave(userId, conversationId);
    }
}
