package com.chat.server.service.conversation;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationMessageService {
    ConversationMessage saveMessage(Long userId,
                                    Long conversationId,
                                    String message);

    List<ConversationMessageResponse> findBeforeMessage(Long userId,
                                                        Long conversationId,
                                                        Long messageId,
                                                        Pageable pageable);
}
