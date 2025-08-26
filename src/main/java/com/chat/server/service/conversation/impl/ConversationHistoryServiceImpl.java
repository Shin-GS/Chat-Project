package com.chat.server.service.conversation.impl;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.history.ConversationMembershipHistory;
import com.chat.server.domain.entity.converstaion.history.ConversationRoleHistory;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.repository.conversation.history.ConversationMembershipHistoryRepository;
import com.chat.server.domain.repository.conversation.history.ConversationRoleHistoryRepository;
import com.chat.server.service.conversation.ConversationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversationHistoryServiceImpl implements ConversationHistoryService {
    private final ConversationMembershipHistoryRepository conversationMembershipHistoryRepository;
    private final ConversationRoleHistoryRepository conversationRoleHistoryRepository;

    @Override
    @Transactional
    public void join(User user,
                     Conversation conversation,
                     ConversationUserRole role) {
        join(user, conversation, role, user);
    }

    @Override
    @Transactional
    public void join(User user,
                     Conversation conversation,
                     ConversationUserRole role,
                     User actor) {
        if (user == null || conversation == null || role == null) {
            return;
        }

        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofNew(conversation, user, role, actor));
        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofJoin(conversation, user, actor));
    }

    @Override
    @Transactional
    public void leave(User user,
                      Conversation conversation,
                      ConversationUserRole beforeRole) {
        leave(user, conversation, beforeRole, user);
    }

    @Override
    @Transactional
    public void leave(User user,
                      Conversation conversation,
                      ConversationUserRole beforeRole,
                      User actor) {
        if (user == null || conversation == null || beforeRole == null) {
            return;
        }

        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofLeave(conversation, user, beforeRole, actor));
        conversationMembershipHistoryRepository.save(ConversationMembershipHistory.ofLeave(conversation, user, actor));
    }

    @Override
    @Transactional
    public void changeRole(User user,
                           Conversation conversation,
                           ConversationUserRole oldRole,
                           ConversationUserRole newRole,
                           User actorUser) {
        if (user == null || conversation == null || oldRole == null || newRole == null) {
            return;
        }

        conversationRoleHistoryRepository.save(ConversationRoleHistory.ofChange(conversation, user, oldRole, newRole, actorUser));
    }
}
