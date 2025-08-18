package com.chat.server.domain.repository.conversation.history;

import com.chat.server.domain.entity.converstaion.history.ConversationMembershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMembershipHistoryRepository extends JpaRepository<ConversationMembershipHistory, Long> {
}
