package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationOneToOneService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationParticipantInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationOneToOneService conversationOneToOneService;
    private final ConversationGroupService conversationGroupService;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationInfoResponse> findConversations(UserId userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return conversationRepository.findAllByUserIdOrderLastActivityAt(userId).stream()
                .map(conversation -> {
                    if (conversation.getType() == ConversationType.ONE_TO_ONE) {
                        return ConversationInfoResponse.of(conversation, conversationOneToOneService.getOneToOneTitle(conversation.getConversationId(), userId));
                    }

                    return ConversationInfoResponse.of(conversation, conversationGroupService.getGroupTitle(conversation.getConversationId()));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationInfoResponse getAccessibleConversation(ConversationId conversationId,
                                                              UserId userId) {
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));

        // oneToOne conversation
        if (conversation.getType() == ConversationType.ONE_TO_ONE) {
            return ConversationInfoResponse.of(conversation, conversationOneToOneService.getOneToOneTitle(conversation.getConversationId(), userId));
        }

        // group conversation
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(conversation.getConversationId(), userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return ConversationInfoResponse.of(conversation, conversationGroupService.getGroupTitle(conversation.getConversationId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserId> findParticipantUserIds(ConversationId conversationId) {
        return conversationParticipantRepository.findAllByConversationId(conversationId).stream()
                .map(ConversationParticipant::getUserId)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationParticipantInfoResponse> findParticipants(ConversationId conversationId) {
        return conversationParticipantRepository.findDtoAllByConversationId(conversationId).stream()
                .map(ConversationParticipantInfoResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public void leave(UserId userId,
                      ConversationId conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (conversation.getType() == ConversationType.ONE_TO_ONE) {
            conversationOneToOneService.leave(userId, conversationId);
            return;
        }

        conversationGroupService.leave(userId, conversationId);
    }
}
