package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.history.ConversationMembershipHistory;
import com.chat.server.domain.entity.converstaion.history.ConversationRoleHistory;
import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.history.ConversationMembershipHistoryRepository;
import com.chat.server.domain.repository.conversation.history.ConversationRoleHistoryRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationOneToOneKeyRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.repository.user.UserRepository;
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
    private final ConversationMembershipHistoryRepository conversationMembershipHistoryRepository;
    private final ConversationRoleHistoryRepository conversationRoleHistoryRepository;

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
    public Long joinOneToOne(Long userId,
                             Long friendUserId) {
        if (userId == null || friendUserId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return create(userId, ConversationType.ONE_TO_ONE, Set.of(friendUserId), null);
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
        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(conversation, user, ConversationUserRole.MEMBER));
        conversationParticipantRepository.save(ConversationParticipant.ofMember(conversation, user));
        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(conversation, user));
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
        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofLeave(conversation, user, participant.getRole()));
        conversationParticipantRepository.delete(participant);
        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofLeave(conversation, user));
    }

    @Override
    @Transactional
    public Long create(Long userId,
                       ConversationType type,
                       Set<Long> userIds,
                       String title) {
        if (userId == null || type == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        return switch (type) {
            case ONE_TO_ONE -> createOneToOne(creator, userIds);
            case GROUP -> createGroup(creator, userIds, title);
        };
    }

    private Long createOneToOne(User creator,
                                Set<Long> userIds) {
        if (ObjectUtils.isEmpty(userIds)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Long targetUserId = userIds.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (targetUserId == null || targetUserId.equals(creator.getId())) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        long smallUserId = Math.min(creator.getId(), targetUser.getId());
        long largeUserId = Math.max(creator.getId(), targetUser.getId());
        Optional<Conversation> existsConversationOptional = conversationRepository.findOneToOneConversationByPair(smallUserId, largeUserId);
        if (existsConversationOptional.isPresent()) {
            Conversation existingConversation = existsConversationOptional.get();
            Stream.of(creator, targetUser)
                    .filter(user -> !conversationParticipantRepository.existsByConversationIdAndUserId(existingConversation.getId(), user.getId()))
                    .forEach(user -> {
                        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(existingConversation, user));
                        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(existingConversation, user, ConversationUserRole.SUPER_ADMIN, creator));
                        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(existingConversation, user, creator));
                    });
            existingConversation.updateActivity();
            return existingConversation.getId();
        }

        Conversation newConversation = conversationRepository.save(Conversation.ofOneToOne(creator));
        conversationOneToOneKeyRepository.save(ConversationOneToOneKey.of(newConversation, creator, targetUser));

        // creator, participants
        List.of(creator, targetUser)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(newConversation, user));
                    conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(newConversation, user, ConversationUserRole.SUPER_ADMIN, creator));
                    conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(newConversation, user, creator));
                });
        return newConversation.getId();
    }

    private Long createGroup(User creator,
                             Set<Long> userIds,
                             String title) {
        if (!StringUtils.hasText(title)) {
            throw new CustomException(ErrorCode.CONVERSATION_NAME_REQUIRED);
        }

        Conversation conversation = conversationRepository.save(Conversation.ofGroup(creator, title));

        // creator
        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(conversation, creator));
        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(conversation, creator, ConversationUserRole.SUPER_ADMIN));
        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(conversation, creator));

        // participants
        if (userIds == null) {
            return conversation.getId();
        }

        List<Long> userIdsExcludeCreator = userIds.stream()
                .filter(Objects::nonNull)
                .filter(id -> !id.equals(creator.getId()))
                .distinct()
                .toList();
        if (ObjectUtils.isEmpty(userIdsExcludeCreator)) {
            return conversation.getId();
        }

        userRepository.findAllById(userIdsExcludeCreator)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofMember(conversation, user));
                    conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(conversation, user, ConversationUserRole.MEMBER, creator));
                    conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(conversation, user, creator));
                });
        return conversation.getId();
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
