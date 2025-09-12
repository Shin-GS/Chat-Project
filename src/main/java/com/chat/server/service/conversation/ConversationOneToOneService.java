package com.chat.server.service.conversation;

import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;

public interface ConversationOneToOneService {
    ConversationId join(UserId requestUserId,
                        UserId targetUserId);

    void leave(UserId userId,
               ConversationId conversationId);

    boolean isUserLeft(UserId userId,
                       ConversationId conversationId);

    UserId getOtherUserId(UserId userId,
                          ConversationId conversationId);

    String getOneToOneTitle(ConversationId conversationId,
                            UserId userId);

    String getOneToOneProfileImageUrl(ConversationId conversationId,
                                      UserId userId);
}
