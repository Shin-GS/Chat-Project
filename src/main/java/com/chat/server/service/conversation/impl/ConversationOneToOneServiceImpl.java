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
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationHistoryService;
import com.chat.server.service.conversation.ConversationMessageService;
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
    private final ConversationMessageService conversationMessageService;

    @Override
    @Transactional
    public ConversationId join(UserId requestUserId,
                               UserId targetUserId) {
        if (requestUserId == null || targetUserId == null || requestUserId.equals(targetUserId)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User requestUser = userRepository.findById(requestUserId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        User targetUser = userRepository.findById(targetUserId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        UserId smallUserId = UserId.of(Math.min(requestUser.getId(), targetUser.getId()));
        UserId largeUserId = UserId.of(Math.max(requestUser.getId(), targetUser.getId()));

        Optional<Conversation> OptionalConversation = conversationRepository.findOneToOneConversationByPair(smallUserId, largeUserId);

        // conversation exists
        if (OptionalConversation.isPresent()) {
            Conversation existingConversation = OptionalConversation.get();
            Long recentlyMessageId = conversationMessageService.findRecentlyMessageId(existingConversation.getConversationId());
            Stream.of(requestUser, targetUser)
                    .filter(user -> !conversationParticipantRepository.existsByConversationIdAndUserId(existingConversation.getConversationId(), user.getUserId()))
                    .forEach(user -> {
                        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(existingConversation, user, recentlyMessageId));
                        conversationHistoryService.join(user, existingConversation, ConversationUserRole.SUPER_ADMIN, requestUser);
                    });
            existingConversation.updateActivity();
            return existingConversation.getConversationId();
        }

        // conversation not exists
        Conversation newConversation = conversationRepository.save(Conversation.ofOneToOne(requestUser));
        conversationOneToOneKeyRepository.save(ConversationOneToOneKey.of(newConversation, requestUser, targetUser));
        List.of(requestUser, targetUser)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(newConversation, user, null));
                    conversationHistoryService.join(user, newConversation, ConversationUserRole.SUPER_ADMIN, requestUser);
                });
        return newConversation.getConversationId();
    }

    @Override
    @Transactional
    public void leave(UserId userId,
                      ConversationId conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (conversation.getType() != ONE_TO_ONE) {
            throw new CustomException(ErrorCode.CONVERSATION_ONE_TO_ONE_ONLY_ALLOWED);
        }

        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationIdAndUserId(conversation.getConversationId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
        User user = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        ConversationUserRole beforeRole = participant.getRole();
        conversationParticipantRepository.delete(participant);
        conversationHistoryService.leave(user, conversation, beforeRole);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserLeft(UserId userId,
                              ConversationId conversationId) {
        return !conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserId getOtherUserId(UserId userId,
                                 ConversationId conversationId) {
        return conversationOneToOneKeyRepository.findOtherUserId(conversationId, userId)
                .map(UserId::of)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
    }

    @Override
    @Transactional(readOnly = true)
    public String getOneToOneTitle(ConversationId conversationId,
                                   UserId userId) {
        return conversationOneToOneKeyRepository.findOtherUsername(conversationId, userId)
                .orElse("Deleted user");
    }
}
