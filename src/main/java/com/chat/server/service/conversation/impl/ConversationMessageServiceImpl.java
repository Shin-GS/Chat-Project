package com.chat.server.service.conversation.impl;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.converstaion.message.ConverstaionMessage;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.*;
import com.chat.server.domain.repository.user.UserRepository;
import com.chat.server.service.conversation.ConversationMessageService;
import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationMessageServiceImpl implements ConversationMessageService {
    private final ConversationMessageRepository conversationMessageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ConversationMessageResponse saveMessage(Long userId,
                                                   ConversationMessageRequest messageRequest) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        User receiver = userRepository.findById(messageRequest.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
        ConverstaionMessage converstaionMessage = conversationMessageRepository.save(ConverstaionMessage.of(sender, receiver, messageRequest.message()));
        return ConversationMessageResponse.of(converstaionMessage, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationMessageResponse> findBeforeMessage(Long userId,
                                                               Long friendUserId,
                                                               Long messageId,
                                                               Pageable pageable) {
        return conversationMessageRepository.findBeforeMessagesBetweenUserIds(userId, friendUserId, messageId, pageable).stream()
                .sorted(Comparator.comparing(ConverstaionMessage::getId))
                .map(chat -> ConversationMessageResponse.of(chat, userId))
                .toList();
    }
}
