package com.chat.server.domain.repository.conversation.query;

import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.vo.UserId;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ConversationQueryRepository {
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
}
