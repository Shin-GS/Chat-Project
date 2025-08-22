package com.chat.server.service.conversation;

import com.chat.server.domain.vo.UserId;

public interface ConversationOneToOneService {
    Long join(UserId requestUserId,
              UserId targetUserId);

    void leave(UserId userId,
               Long conversationId);

    boolean isUserLeft(UserId userId,
                       Long conversationId);

    UserId getOtherUserId(UserId userId,
                          Long conversationId);
}
