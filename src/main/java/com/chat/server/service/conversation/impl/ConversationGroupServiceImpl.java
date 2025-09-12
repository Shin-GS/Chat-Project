package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationHistoryService;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.chat.server.common.constant.conversation.ConversationType.GROUP;
import static com.chat.server.common.constant.conversation.ConversationUserRole.SUPER_ADMIN;

@Service
@RequiredArgsConstructor
public class ConversationGroupServiceImpl implements ConversationGroupService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationHistoryService conversationHistoryService;
    private final ConversationMessageService conversationMessageService;

    @Override
    @Transactional
    public ConversationId create(UserId requestUserId,
                                 Set<UserId> targetUserIds,
                                 String title,
                                 String imageUrl,
                                 String joinCode,
                                 boolean hidden) {
        if (requestUserId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        if (!StringUtils.hasText(title)) {
            throw new CustomException(ErrorCode.CONVERSATION_NAME_REQUIRED);
        }

        User requestUser = userRepository.findById(requestUserId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation newConversation = conversationRepository.save(Conversation.ofGroup(requestUser, title, imageUrl, joinCode, hidden));

        // requestUser
        conversationParticipantRepository.save(ConversationParticipant.ofSuperAdmin(newConversation, requestUser, null));
        conversationHistoryService.join(requestUser, newConversation, SUPER_ADMIN);

        // participants
        if (targetUserIds == null || targetUserIds.isEmpty()) {
            return newConversation.getConversationId();
        }

        List<Long> targetUserIdsExcludeRequestUserId = targetUserIds.stream()
                .filter(Objects::nonNull)
                .map(UserId::value)
                .filter(id -> !id.equals(requestUser.getId()))
                .distinct()
                .toList();
        if (ObjectUtils.isEmpty(targetUserIdsExcludeRequestUserId)) {
            return newConversation.getConversationId();
        }

        userRepository.findAllById(targetUserIdsExcludeRequestUserId)
                .forEach(user -> {
                    conversationParticipantRepository.save(ConversationParticipant.ofMember(newConversation, user, null));
                    conversationHistoryService.join(user, newConversation, ConversationUserRole.MEMBER, requestUser);
                });
        return newConversation.getConversationId();
    }

    @Override
    @Transactional
    public ConversationId join(UserId userId,
                               String joinCode,
                               ConversationId conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User user = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (conversation.getType() != GROUP) {
            throw new CustomException(ErrorCode.CONVERSATION_NOT_GROUP);
        }

        if (conversationParticipantRepository.existsByConversationIdAndUserId(conversation.getConversationId(), user.getUserId())) {
            throw new CustomException(ErrorCode.CONVERSATION_ALREADY_JOINED);
        }

        if (conversation.hasCode() && (!StringUtils.hasText(joinCode) || !Objects.equals(conversation.getJoinCode(), joinCode))) {
            throw new CustomException(ErrorCode.CONVERSATION_JOIN_CODE_IS_INVALID);
        }

        conversation.updateActivity();
        Long recentlyMessageId = conversationMessageService.findRecentlyMessageId(conversation.getConversationId());
        conversationParticipantRepository.save(ConversationParticipant.ofMember(conversation, user, recentlyMessageId));
        conversationHistoryService.join(user, conversation, ConversationUserRole.MEMBER);

        // notice
        conversationMessageService.handleSystemMessage(
                user.getUserId(),
                conversation.getConversationId(),
                "%s has joined".formatted(user.getUsername()),
                null);
        return conversation.getConversationId();
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
        if (conversation.getType() != GROUP) {
            throw new CustomException(ErrorCode.CONVERSATION_GROUP_ONLY_ALLOWED);
        }

        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationIdAndUserId(conversation.getConversationId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
        if (participant.getRole() == SUPER_ADMIN
                && !conversationParticipantRepository.existsByConversationIdAndRoleAndUserIdNot(conversation.getConversationId(), SUPER_ADMIN, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_SUPER_ADMIN_REQUIRED);
        }

        User user = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        conversation.updateActivity();
        ConversationUserRole beforeRole = participant.getRole();
        conversationParticipantRepository.delete(participant);
        conversationHistoryService.leave(user, conversation, beforeRole);

        conversationMessageService.handleSystemMessage(
                user.getUserId(),
                conversation.getConversationId(),
                "%s has leaved".formatted(user.getUsername()),
                null);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPageResponse<ConversationInfoResponse> findConversations(UserId userId,
                                                                          String keyword,
                                                                          Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return CustomPageResponse.emptyPage(pageable);
        }

        Page<ConversationDto> dtoPage = conversationRepository.searchJoinAbleGroups(userId, keyword, pageable);
        return CustomPageResponse.fromPage(dtoPage, ConversationInfoResponse::of);
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationInfoResponse getJoinAbleConversation(ConversationId conversationId,
                                                            UserId userId) {
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        if (conversation.getType() != GROUP) {
            throw new CustomException(ErrorCode.CONVERSATION_GROUP_ONLY_ALLOWED);
        }

        if (conversation.isHidden()) {
            throw new CustomException(ErrorCode.CONVERSATION_IS_HIDDEN);
        }

        if (conversationParticipantRepository.existsByConversationIdAndUserId(conversation.getConversationId(), userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_ALREADY_JOINED);
        }

        return ConversationInfoResponse.of(conversation, getGroupTitle(conversation.getConversationId()));
    }

    @Override
    @Transactional(readOnly = true)
    public String getGroupTitle(ConversationId conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        return !StringUtils.hasText(conversation.getTitle()) ? "Untitled group" : conversation.getTitle();
    }

    @Override
    @Transactional
    public void changeRole(UserId requestUserId,
                           ConversationId conversationId,
                           UserId targetUserId,
                           ConversationUserRole role) {
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        if (conversation.getType() != GROUP) {
            throw new CustomException(ErrorCode.CONVERSATION_GROUP_ONLY_ALLOWED);
        }

        User requestUser = userRepository.findById(requestUserId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        ConversationParticipant requestParticipant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, requestUser.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_PARTICIPANT_NOT_EXISTS));
        if (requestParticipant.getRole() != SUPER_ADMIN) {
            throw new CustomException(ErrorCode.CONVERSATION_SUPER_ADMIN_REQUIRED);
        }

        User targetUser = userRepository.findById(targetUserId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        ConversationParticipant targetParticipant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, targetUser.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_PARTICIPANT_NOT_EXISTS));
        if (targetParticipant.getRole() == role) {
            throw new CustomException(ErrorCode.CONVERSATION_ROLE_ALREADY_EXISTS);
        }

        if (targetParticipant.getRole() == SUPER_ADMIN
                && !conversationParticipantRepository.existsByConversationIdAndRoleAndUserIdNot(conversation.getConversationId(), SUPER_ADMIN, targetUser.getUserId())) {
            throw new CustomException(ErrorCode.CONVERSATION_SUPER_ADMIN_REQUIRED);
        }

        ConversationUserRole beforeRole = targetParticipant.getRole();
        targetParticipant.changeRole(role);
        conversationHistoryService.changeRole(targetUser, conversation, beforeRole, role, requestUser);

        conversationMessageService.handleSystemMessage(
                requestUser.getUserId(),
                conversation.getConversationId(),
                "%s role changed to %s".formatted(targetUser.getUsername(), role),
                List.of("refresh-conversation-participant-list"));
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationUserRole getRole(ConversationId conversationId,
                                        UserId userId) {
        return conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId)
                .map(ConversationParticipant::getRole)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_PARTICIPANT_NOT_EXISTS));
    }
}
