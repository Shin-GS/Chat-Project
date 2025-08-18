package com.chat.server.domain.repository.conversation;

import com.chat.server.domain.entity.converstaion.participant.ConversationOneToOneKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationOneToOneKeyRepository extends JpaRepository<ConversationOneToOneKey, Long> {
}
