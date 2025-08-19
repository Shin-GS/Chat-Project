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
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.chat.server.common.constant.conversation.ConversationType.GROUP;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationOneToOneKeyRepository conversationOneToOneKeyRepository;
    private final ConversationHistoryService conversationHistoryService;

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
    @Transactional
    public Long joinOneToOne(Long requestUserId,
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
    public Long joinGroup(Long userId,
                          Long conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (conversation.getType() != GROUP) {
            throw new CustomException(ErrorCode.CONVERSATION_NOT_GROUP);
        }

        if (conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_ALREADY_JOINED);
        }

        // todo 새 멤버가 들어왔습니다.
        conversation.updateActivity();
        conversationParticipantRepository.save(ConversationParticipant.ofMember(conversation, user));
        conversationHistoryService.join(user, conversation, ConversationUserRole.MEMBER);
        return conversation.getId();
    }

    @Override
    @Transactional
    public void leave(Long userId,
                      Long conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
        if (ConversationUserRole.SUPER_ADMIN.equals(participant.getRole())
                && !conversationParticipantRepository.existsByConversationIdAndRoleAndUserIdNot(conversationId, ConversationUserRole.SUPER_ADMIN, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_SUPER_ADMIN_REQUIRED);
        }

        // todo 멤버가 대화방을 나갔습니다.
        conversation.updateActivity();
        ConversationUserRole beforeRole = participant.getRole();
        conversationParticipantRepository.delete(participant);
        conversationHistoryService.leave(user, conversation, beforeRole);
    }

    @Override
    @Transactional
    public Long createGroup(Long requestUserId,
                            Set<Long> targetUserIds,
                            String title,
                            String joinCode,
                            boolean hidden) {
        if (requestUserId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        if (!StringUtils.hasText(title)) {
            throw new CustomException(ErrorCode.CONVERSATION_NAME_REQUIRED);
        }

        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation newConversation = conversationRepository.save(Conversation.ofGroup(requestUser, title, joinCode, hidden));

        // requestUser
        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(newConversation, requestUser));
        conversationHistoryService.join(requestUser, newConversation, ConversationUserRole.SUPER_ADMIN);

        // participants
        if (targetUserIds == null || targetUserIds.isEmpty()) {
            return newConversation.getId();
        }

        List<Long> targetUserIdsExcludeRequestUserId = targetUserIds.stream()
                .filter(Objects::nonNull)
                .filter(id -> !id.equals(requestUser.getId()))
                .distinct()
                .toList();
        if (ObjectUtils.isEmpty(targetUserIdsExcludeRequestUserId)) {
            return newConversation.getId();
        }

        userRepository.findAllById(targetUserIdsExcludeRequestUserId)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofMember(newConversation, user));
                    conversationHistoryService.join(user, newConversation, ConversationUserRole.MEMBER, requestUser);
                });
        return newConversation.getId();
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
