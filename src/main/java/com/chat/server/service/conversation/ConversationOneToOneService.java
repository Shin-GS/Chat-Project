package com.chat.server.service.conversation;

public interface ConversationOneToOneService {
    Long join(Long requestUserId,
              Long targetUserId);

    void leave(Long userId,
               Long conversationId);

    boolean isUserLeft(Long userId,
                       Long conversationId);

    Long getOtherUserId(Long userId,
                        Long conversationId);
}
