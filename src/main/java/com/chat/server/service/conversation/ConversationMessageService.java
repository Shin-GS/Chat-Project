package com.chat.server.service.conversation;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationMessageService {
    ConversationMessage saveMessage(UserId userId,
                                    ConversationId conversationId,
                                    String message);

    List<ConversationMessageResponse> findBeforeMessage(UserId userId,
                                                        ConversationId conversationId,
                                                        Long messageId,
                                                        Pageable pageable);

    Long findRecentlyMessageId(ConversationId conversationId);
}
