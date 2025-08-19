package com.chat.server.service.conversation;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.user.User;

public interface ConversationHistoryService {
    void join(User user,
              Conversation conversation,
              ConversationUserRole role);

    void join(User user,
              Conversation conversation,
              ConversationUserRole role,
              User actor);

    void leave(User user,
               Conversation conversation,
               ConversationUserRole beforeRole);

    void leave(User user,
               Conversation conversation,
               ConversationUserRole beforeRole,
               User actor);
}
