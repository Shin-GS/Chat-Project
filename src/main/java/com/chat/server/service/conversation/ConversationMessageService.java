package com.chat.server.service.conversation;

import com.chat.server.service.conversation.request.ConversationMessageRequest;
import com.chat.server.service.conversation.response.ConversationMessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConversationMessageService {
    ConversationMessageResponse saveMessage(Long userId,
                                            ConversationMessageRequest messageRequest);

    List<ConversationMessageResponse> findBeforeMessage(Long userId,
                                                        Long friendUserId,
                                                        Long messageId,
                                                        Pageable pageable);
}
