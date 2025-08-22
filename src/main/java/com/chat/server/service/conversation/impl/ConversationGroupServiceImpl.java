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
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.common.response.CustomPageResponse;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationHistoryService;
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

@Service
@RequiredArgsConstructor
public class ConversationGroupServiceImpl implements ConversationGroupService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationHistoryService conversationHistoryService;

    @Override
    @Transactional
    public Long create(UserId requestUserId,
                       Set<UserId> targetUserIds,
                       String title,
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
                .map(UserId::value)
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
    @Transactional
    public Long join(UserId userId,
                     Long conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User user = userRepository.findById(userId.value())
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
    public void leave(UserId userId,
                      Long conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_GROUP_NOT_EXISTS));
        if (!conversation.getType().equals(GROUP)) {
            throw new CustomException(ErrorCode.CONVERSATION_GROUP_ONLY_ALLOWED);
        }

        ConversationParticipant participant = conversationParticipantRepository
                .findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_MEMBER));
        if (ConversationUserRole.SUPER_ADMIN.equals(participant.getRole())
                && !conversationParticipantRepository.existsByConversationIdAndRoleAndUserIdNot(conversationId, ConversationUserRole.SUPER_ADMIN, userId)) {
            throw new CustomException(ErrorCode.CONVERSATION_SUPER_ADMIN_REQUIRED);
        }

        User user = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        // todo 멤버가 대화방을 나갔습니다.
        conversation.updateActivity();
        ConversationUserRole beforeRole = participant.getRole();
        conversationParticipantRepository.delete(participant);
        conversationHistoryService.leave(user, conversation, beforeRole);
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
}
