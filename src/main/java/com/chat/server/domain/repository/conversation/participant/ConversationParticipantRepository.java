package com.chat.server.domain.repository.conversation.participant;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.dto.ConversationParticipantDto;
import com.chat.server.domain.entity.converstaion.participant.ConversationParticipant;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {
    boolean existsByConversationIdAndUserId(ConversationId conversationId,
                                            UserId userId);

    Optional<ConversationParticipant> findByConversationIdAndUserId(ConversationId conversationId,
                                                                    UserId userId);

    boolean existsByConversationIdAndRoleAndUserIdNot(ConversationId conversationId,
                                                      ConversationUserRole role,
                                                      UserId userId);

    List<ConversationParticipant> findAllByConversationId(ConversationId conversationId);


    @Query("""
            SELECT new com.chat.server.domain.dto.ConversationParticipantDto(
                participant.userId,
                participantUser.username,
                participant.role)
            FROM ConversationParticipant participant
            JOIN User participantUser ON participantUser.id = participant.userId.value
            WHERE participant.conversationId.value = :#{#conversationId.value}
            """)
    List<ConversationParticipantDto> findDtoAllByConversationId(ConversationId conversationId);
}
