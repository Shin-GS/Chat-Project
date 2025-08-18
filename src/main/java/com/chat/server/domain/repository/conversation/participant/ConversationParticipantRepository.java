package com.chat.server.domain.repository.conversation.participant;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    Optional<ConversationParticipant> findByConversationIdAndUserId(Long conversationId, Long userId);

    boolean existsByConversationIdAndRoleAndUserIdNot(Long conversationId, ConversationUserRole role, Long userId);

    List<ConversationParticipant> findAllByConversationId(Long conversationId);
}
