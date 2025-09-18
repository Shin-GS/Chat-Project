package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.entity.converstaion.sticker.Sticker;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.message.ConversationMessageRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.event.message.ConversationMessageEvent;
import com.chat.server.event.message.RefreshConversationUiEvent;
import com.chat.server.event.message.SystemMessageEvent;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.ConversationStickerService;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.chat.server.common.constant.Constants.SOCKET_DESTINATION_CONVERSATION_SYSTEM_MESSAGE;

@Service
@RequiredArgsConstructor
public class ConversationMessageServiceImpl implements ConversationMessageService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationMessageRepository conversationMessageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConversationStickerService conversationStickerService;

    @Override
    @Transactional
    public void handleMessage(UserId userId,
                              ConversationId conversationId,
                              ConversationMessageType type,
                              String message,
                              Long stickerId) {
        if (userId == null
                || conversationId == null
                || type == null
                || (type == ConversationMessageType.TEXT && message == null)
                || (type == ConversationMessageType.STICKER && stickerId == null)) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User sender = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));

        // Save Message
        ConversationMessage savedMessage = switch (type) {
            case TEXT -> conversationMessageRepository.save(ConversationMessage.ofText(sender, conversation, message));
            case STICKER -> {
                Sticker sticker = conversationStickerService.getStickerById(stickerId);
                yield conversationMessageRepository.save(ConversationMessage.ofSticker(sender, conversation, sticker));
            }
            default -> throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        };

        conversation.updateActivity();
        conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId)
                .ifPresent(participant -> readMessage(participant.getUserId(), conversation.getConversationId())); // 메시지를 보냈으나 나간 케이스도 존재함

        applicationEventPublisher.publishEvent(ConversationMessageEvent.of(
                savedMessage.getConversationId(),
                userId,
                savedMessage.getId())
        );
        applicationEventPublisher.publishEvent(RefreshConversationUiEvent.of(savedMessage.getConversationId()));
    }

    @Override
    @Transactional
    public void handleSystemMessage(UserId userId,
                                    ConversationId conversationId,
                                    String message,
                                    List<String> refreshIds) {
        if (userId == null || conversationId == null || message == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User sender = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        ConversationMessage savedMessage = conversationMessageRepository.save(ConversationMessage.ofSystem(sender, conversation, message));
        conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId)
                .ifPresent(participant -> readMessage(participant.getUserId(), conversation.getConversationId())); // 메시지를 보냈으나 나간 케이스도 존재함

        applicationEventPublisher.publishEvent(
                SystemMessageEvent.of(
                        conversation.getConversationId(),
                        savedMessage.getId(),
                        SOCKET_DESTINATION_CONVERSATION_SYSTEM_MESSAGE.formatted(conversation.getConversationId()),
                        refreshIds
                )
        );
        applicationEventPublisher.publishEvent(RefreshConversationUiEvent.of(savedMessage.getConversationId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationMessageResponse> findBeforeMessage(UserId userId,
                                                               ConversationId conversationId,
                                                               Long messageId,
                                                               Pageable pageable) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Optional<ConversationParticipant> optionalParticipant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);
        if (optionalParticipant.isEmpty()) {
            throw new CustomException(ErrorCode.CONVERSATION_NOT_JOINED);
        }

        ConversationParticipant participant = optionalParticipant.get();
        return conversationMessageRepository.findBeforeMessages(conversationId, messageId, participant.getJoinMessageId(), pageable).stream()
                .sorted(Comparator.comparing(ConversationMessage::getId))
                .map(chat -> ConversationMessageResponse.of(chat, userId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findRecentlyMessageId(ConversationId conversationId) {
        return conversationMessageRepository.findMaxMessageIdByConversationId(conversationId);
    }

    @Transactional
    public void readMessage(UserId userId,
                            ConversationId conversationId) {
        if (userId == null || conversationId == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        Optional<ConversationParticipant> optionalParticipant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);
        if (optionalParticipant.isEmpty()) {
            throw new CustomException(ErrorCode.CONVERSATION_NOT_JOINED);
        }

        ConversationParticipant participant = optionalParticipant.get();
        conversationMessageRepository.findMaxMessageByConversationId(conversationId)
                .ifPresent(participant::readMessage);
    }
}
