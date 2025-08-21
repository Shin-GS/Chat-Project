package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationOneToOneKeyRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.service.conversation.ConversationHistoryService;
import com.chat.server.service.conversation.ConversationOneToOneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.chat.server.common.constant.conversation.ConversationType.ONE_TO_ONE;

@Service
@RequiredArgsConstructor
public class ConversationOneToOneServiceImpl implements ConversationOneToOneService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationOneToOneKeyRepository conversationOneToOneKeyRepository;
    private final ConversationHistoryService conversationHistoryService;

    @Override
    @Transactional
    public Long join(Long requestUserId,
                     Long targetUserId) {
        if (requestUserId == null || targetUserId == null || requestUserId.equals(targetUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Optional<Conversation> OptionalConversation = conversationRepository.findOneToOneConversationByPair(
                Math.min(requestUser.getId(), targetUser.getId()),
                Math.max(requestUser.getId(), targetUser.getId())
        );

        // conversation exists
        if (OptionalConversation.isPresent()) {
            Conversation existingConversation = OptionalConversation.get();
            Stream.of(requestUser, targetUser)
                    .filter(user -> !conversationParticipantRepository.existsByConversationIdAndUserId(existingConversation.getId(), user.getId()))
                    .forEach(user -> {
                        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(existingConversation, user));
                        conversationHistoryService.join(user, existingConversation, ConversationUserRole.SUPER_ADMIN, requestUser);
                    });
            existingConversation.updateActivity();
            return existingConversation.getId();
        }

        // conversation not exists
        Conversation newConversation = conversationRepository.save(Conversation.ofOneToOne(requestUser));
        conversationOneToOneKeyRepository.save(ConversationOneToOneKey.of(newConversation, requestUser, targetUser));
        List.of(requestUser, targetUser)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(newConversation, user));
                    conversationHistoryService.join(user, newConversation, ConversationUserRole.SUPER_ADMIN, requestUser);
                });
        return newConversation.getId();
    }

    @Override
    @Transactional
    public void leave(Long userId,
                      Long conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (!conversation.getType().equals(ONE_TO_ONE)) {
            throw new CustomException(ErrorCode.CONVERSATION_ONE_TO_ONE_ONLY_ALLOWED);
        }

        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        ConversationUserRole beforeRole = participant.getRole();
        conversationParticipantRepository.delete(participant);
        conversationHistoryService.leave(user, conversation, beforeRole);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserLeft(Long userId,
                              Long conversationId) {
        return !conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOtherUserId(Long userId,
                               Long conversationId) {
        return conversationOneToOneKeyRepository.findOtherUserId(conversationId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
    }
}
