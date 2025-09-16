package com.chat.server.domain.repository.conversation.query.impl;

import com.chat.server.domain.entity.converstaion.message.ConversationMessage;
import com.chat.server.domain.repository.conversation.query.ConversationMessageQueryRepository;
import com.chat.server.domain.vo.ConversationId;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.chat.server.domain.entity.converstaion.message.QConversationMessage.conversationMessage;

@Repository
@RequiredArgsConstructor
public class ConversationMessageQueryRepositoryImpl implements ConversationMessageQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConversationMessage> findBeforeMessages(ConversationId conversationId,
                                                        Long maxMessageId,
                                                        Long joinMessageId,
                                                        Pageable pageable) {
        return queryFactory
                .selectFrom(conversationMessage)
                .where(conversationMessage.conversationId.value.eq(conversationId.value()),
                        gtMessageIdIfPresent(joinMessageId),
                        ltMessageIdIfPresent(maxMessageId))
                .offset(pageable.getOffset())
                .orderBy(conversationMessage.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression gtMessageIdIfPresent(Long messageId) {
        return (messageId == null) ? null : conversationMessage.id.gt(messageId);
    }

    private BooleanExpression ltMessageIdIfPresent(Long messageId) {
        return (messageId == null) ? null : conversationMessage.id.lt(messageId);
    }

    @Override
    public Long findMaxMessageIdByConversationId(ConversationId conversationId) {
        return queryFactory
                .select(conversationMessage.id.max())
                .from(conversationMessage)
                .where(conversationMessage.conversationId.value.eq(conversationId.value()))
                .fetchOne();
    }

    @Override
    public Optional<ConversationMessage> findMaxMessageByConversationId(ConversationId conversationId) {
        var latestMessage = queryFactory
                .selectFrom(conversationMessage)
                .where(conversationMessage.conversationId.value.eq(conversationId.value()))
                .orderBy(conversationMessage.id.desc())
                .fetchFirst();
        return Optional.ofNullable(latestMessage);
    }
}
