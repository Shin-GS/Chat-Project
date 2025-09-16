package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.common.exception.CustomException;
import com.chat.server.common.util.Base64Util;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.message.ConversationMessageRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.common.CommonFileUploadService;
import com.chat.server.service.conversation.ConversationGroupService;
import com.chat.server.service.conversation.ConversationOneToOneService;
import com.chat.server.service.conversation.ConversationService;
import com.chat.server.service.conversation.response.ConversationInfoAndMessageResponse;
import com.chat.server.service.conversation.response.ConversationInfoResponse;
import com.chat.server.service.conversation.response.ConversationParticipantInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.chat.server.common.constant.Constants.ORDER_CONVERSATION_ACTIVITY_AT;
import static org.hibernate.query.SortDirection.DESCENDING;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationOneToOneService conversationOneToOneService;
    private final ConversationGroupService conversationGroupService;
    private final ConversationMessageRepository conversationMessageRepository;
    private final CommonFileUploadService commonFileUploadService;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationInfoAndMessageResponse> findConversations(UserId userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        return conversationRepository.findAllBy(userId, ORDER_CONVERSATION_ACTIVITY_AT, DESCENDING).stream()
                .map(conversation -> convertConversationInfoAndMessageResponse(userId, conversation))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationInfoAndMessageResponse findConversation(UserId userId,
                                                               ConversationId conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        return convertConversationInfoAndMessageResponse(userId, conversation);
    }

    private ConversationInfoAndMessageResponse convertConversationInfoAndMessageResponse(UserId userId,
                                                                                         Conversation conversation) {
        ConversationParticipant participant = conversationParticipantRepository.findByConversationIdAndUserId(conversation.getConversationId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_JOINED));
        ConversationMessage lastConversationMessage = conversationMessageRepository.findMaxMessageByConversationId(conversation.getConversationId())
                .orElse(null);
        String lastMessage = lastConversationMessage == null ? "" : switch (lastConversationMessage.getType()) {
            case TEXT, SYSTEM -> lastConversationMessage.getMessage();
            case IMAGE -> "Sent a photo";
            case FILE -> "Sent a file";
        };

        if (conversation.getType() == ConversationType.ONE_TO_ONE) {
            return ConversationInfoAndMessageResponse.ofOneToOne(conversation,
                    conversationOneToOneService.getOtherUserId(userId, conversation.getConversationId()),
                    conversationOneToOneService.getOneToOneTitle(conversation.getConversationId(), userId),
                    conversationOneToOneService.getOneToOneProfileImageUrl(conversation.getConversationId(), userId),
                    lastMessage,
                    isReadMessage(participant, lastConversationMessage));
        }

        return ConversationInfoAndMessageResponse.of(conversation,
                conversationGroupService.getGroupTitle(conversation.getConversationId()),
                conversation.getImageUrl(),
                lastMessage,
                isReadMessage(participant, lastConversationMessage));
    }

    private boolean isReadMessage(ConversationParticipant participant,
                                  ConversationMessage lastConversationMessage) {
        if (lastConversationMessage == null) {
            // No messages in this conversation -> nothing to read
            return true;
        }

        // Treat messages before join point as irrelevant/read
        Long joinMessageId = participant.getJoinMessageId();
        if (joinMessageId != null && lastConversationMessage.getId() < joinMessageId) {
            return true;
        }

        long participantReadMessageId = participant.getLastReadMessageId() == null ? 0L : participant.getLastReadMessageId();
        return participantReadMessageId >= lastConversationMessage.getId();
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
    public List<ConversationParticipantInfoResponse> findParticipants(ConversationId conversationId,
                                                                      UserId userId) {
        return conversationParticipantRepository.findDtoAllByConversationId(conversationId).stream()
                .map(ConversationParticipantInfoResponse::of)
                .sorted(Comparator
                        .comparing((ConversationParticipantInfoResponse participant) -> !participant.userId().equals(userId)) // In Java, when sorting Booleans, false is considered less than true(false < true)
                        .thenComparing(participant -> ConversationUserRole.sortPriority(participant.role()))
                        .thenComparing(ConversationParticipantInfoResponse::name, Comparator.nullsLast(String::compareToIgnoreCase)))
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

    @Override
    public String uploadImage(UserId userId,
                              MultipartFile file) {
        String userIdBase64 = Base64Util.encode(userId.toString());
        String filename = UUID.randomUUID().toString().replace("-", "");
        return commonFileUploadService.uploadFile(
                Constants.UPLOAD_CONVERSATION_IMAGE_SUB_DIR.formatted(userIdBase64),
                file,
                filename,
                Constants.UPLOAD_CONVERSATION_IMAGE_ALLOWED_EXT,
                Constants.UPLOAD_CONVERSATION_IMAGE_MAX_BYTES);
    }
}
