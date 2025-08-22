package com.chat.server.domain.repository.conversation;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
            SELECT conversation
            FROM Conversation conversation
            WHERE EXISTS (
                SELECT 1
                FROM ConversationParticipant participant
                WHERE participant.conversationId.value = conversation.id AND participant.userId.value = :#{#userId.value}
            )
            ORDER BY conversation.lastActivityAt DESC
            """)
    List<Conversation> findAllByUserIdOrderLastActivityAt(@Param("userId") UserId userId);

    @Query("""
            select conversation
            from ConversationOneToOneKey oneToOneKey
            join Conversation conversation on conversation.id = oneToOneKey.conversationId.value
            where oneToOneKey.smallUserId.value = :#{#smallUserId.value} and oneToOneKey.largeUserId.value = :#{#largeUserId.value}
            """)
    Optional<Conversation> findOneToOneConversationByPair(@Param("smallUserId") UserId smallUserId,
                                                          @Param("largeUserId") UserId largeUserId);

    @Query("""
            select new com.chat.server.domain.dto.ConversationDto(
                conversation.id,
                conversation.type,
                COALESCE(conversation.title, 'Untitled group'),
                conversation.lastActivityAt
            )
            from Conversation conversation
            where conversation.type = com.chat.server.common.constant.conversation.ConversationType.GROUP
              and conversation.hidden = false
              and (:keyword is null or :keyword = '' or LOWER(conversation.title) like CONCAT('%', LOWER(:keyword), '%'))
              and not exists (
                select 1
                from ConversationParticipant participant
                where participant.conversationId.value = conversation.id and participant.userId.value = :#{#userId.value}
            )
            order by conversation.lastActivityAt desc, conversation.id desc
            """)
    Page<ConversationDto> searchJoinAbleGroups(@Param("userId") UserId userId,
                                               @Param("keyword") String keyword,
                                               Pageable pageable);

    // Delete Prevention
    @Override
    default void delete(@NonNull Conversation entity) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteById(@NonNull Long aLong) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAll(@NonNull Iterable<? extends Conversation> entities) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAll() {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAllInBatch() {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAllInBatch(@NonNull Iterable<Conversation> entities) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAllById(@NonNull Iterable<? extends Long> longs) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    @Override
    default void deleteAllByIdInBatch(@NonNull Iterable<Long> longs) {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }
}
