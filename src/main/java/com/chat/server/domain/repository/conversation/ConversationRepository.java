package com.chat.server.domain.repository.conversation;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.repository.conversation.query.ConversationQueryRepository;
import com.chat.server.domain.vo.UserId;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>, ConversationQueryRepository {
    List<Conversation> findAllBy(UserId userId,
                                 String order,
                                 SortDirection direction);

    Optional<Conversation> findOneToOneConversation(UserId smallUserId,
                                                    UserId largeUserId);

    boolean existsJoinAbleGroups(UserId userId,
                                 String keyword);

    Page<ConversationDto> searchJoinAbleGroups(UserId userId,
                                               String keyword,
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
