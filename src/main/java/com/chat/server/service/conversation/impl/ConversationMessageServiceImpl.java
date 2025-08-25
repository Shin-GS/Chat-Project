package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.ConversationRepository;
import com.chat.server.domain.repository.conversation.message.ConversationMessageRepository;
import com.chat.server.domain.repository.conversation.participant.ConversationParticipantRepository;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationMessageServiceImpl implements ConversationMessageService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ConversationMessageRepository conversationMessageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ConversationMessage saveMessage(UserId userId,
                                           ConversationId conversationId,
                                           String message) {
        if (userId == null || conversationId == null || message == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User sender = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        return conversationMessageRepository.save(ConversationMessage.of(sender, conversation, ConversationMessageType.TEXT, message));
    }

    @Override
    public ConversationMessage saveSystemMessage(UserId userId,
                                                 ConversationId conversationId,
                                                 String message) {
        if (userId == null || conversationId == null || message == null) {
            throw new CustomException(ErrorCode.CONVERSATION_REQUEST_INVALID);
        }

        User sender = userRepository.findById(userId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        Conversation conversation = conversationRepository.findById(conversationId.value())
                .orElseThrow(() -> new CustomException(ErrorCode.CONVERSATION_NOT_EXISTS));
        return conversationMessageRepository.save(ConversationMessage.of(sender, conversation, ConversationMessageType.SYSTEM, message));
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
    public Long findRecentlyMessageId(ConversationId conversationId) {
        return conversationMessageRepository.findMaxMessageIdByConversation(conversationId);
    }
}
