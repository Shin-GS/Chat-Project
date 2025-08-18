package com.chat.server.domain.repository.conversation.history;

import com.chat.server.domain.entity.converstaion.history.ConversationRoleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRoleHistoryRepository extends JpaRepository<ConversationRoleHistory, Long> {
}
