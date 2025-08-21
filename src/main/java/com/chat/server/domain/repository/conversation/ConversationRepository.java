package com.chat.server.domain.repository.conversation;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
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
    /*
        1:1 → 상대 이름
        1:1(상대 탈퇴) → “Deleted user”
        그룹(이름 O) → 그룹 이름
        그룹(이름 X or 혼자) → “Untitled group”
        그 외 → 미노출
     */
    @Query("""
            SELECT DISTINCT new com.chat.server.domain.dto.ConversationDto(
                conversation.id,
                conversation.type,
                CASE
                     WHEN conversation.type = com.chat.server.common.constant.conversation.ConversationType.ONE_TO_ONE THEN COALESCE(conversationuser.username, 'Deleted user')
                     ELSE COALESCE(conversation.title, 'Untitled group')
                END,
                conversation.lastActivityAt
            )
            FROM Conversation conversation
            LEFT JOIN ConversationParticipant participant2 ON participant2.conversationId = conversation.id AND participant2.userId <> :userId AND conversation.type = com.chat.server.common.constant.conversation.ConversationType.ONE_TO_ONE
            LEFT JOIN User conversationuser ON conversationuser.id = participant2.userId
            WHERE EXISTS (SELECT 1 FROM ConversationParticipant participant1 WHERE participant1.conversationId = conversation.id AND participant1.userId = :userId)
            ORDER BY conversation.lastActivityAt DESC
            """)
    List<ConversationDto> findAllByUserIdOrderLastActivityAt(@Param("userId") Long userId);

    @Query("""
            select conversation
            from ConversationOneToOneKey oneToOneKey
            join Conversation conversation on conversation.id = oneToOneKey.conversationId
            where oneToOneKey.smallUserId = :smallUserId and oneToOneKey.largeUserId = :largeUserId
            """)
    Optional<Conversation> findOneToOneConversationByPair(@Param("smallUserId") Long smallUserId,
                                                          @Param("largeUserId") Long largeUserId);

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
              and not exists (select 1 from ConversationParticipant participant where participant.conversationId = conversation.id and participant.userId = :userId)
            order by conversation.lastActivityAt desc, conversation.id desc
            """)
    Page<ConversationDto> searchJoinAbleGroups(@Param("userId") Long userId,
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
