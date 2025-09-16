package com.chat.server.domain.repository.conversation.query.impl;

import com.chat.server.domain.repository.conversation.query.ConversationOneToOneKeyQueryRepository;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.chat.server.domain.entity.converstaion.participant.QConversationOneToOneKey.conversationOneToOneKey;
import static com.chat.server.domain.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ConversationOneToOneKeyQueryRepositoryImpl implements ConversationOneToOneKeyQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<String> findOtherUsername(ConversationId conversationId,
                                              UserId userId) {
        String otherUsername = queryFactory
                .select(user.username)
                .from(conversationOneToOneKey)
                .join(user).on(otherUserJoinCondition(userId))
                .where(conversationOneToOneKey.conversationId.value.eq(conversationId.value()))
                .fetchFirst();
        return Optional.ofNullable(otherUsername);
    }

    @Override
    public Optional<Long> findOtherUserId(ConversationId conversationId,
                                          UserId userId) {
        Long otherUserId = queryFactory
                .select(user.id)
                .from(conversationOneToOneKey)
                .join(user).on(otherUserJoinCondition(userId))
                .where(conversationOneToOneKey.conversationId.value.eq(conversationId.value()))
                .fetchFirst();
        return Optional.ofNullable(otherUserId);
    }

    @Override
    public Optional<String> findOtherProfileImageUrl(ConversationId conversationId,
                                                     UserId userId) {
        String otherUserProfileImageUrl = queryFactory
                .select(user.profileImageUrl)
                .from(conversationOneToOneKey)
                .join(user).on(otherUserJoinCondition(userId))
                .where(conversationOneToOneKey.conversationId.value.eq(conversationId.value()))
                .fetchFirst();
        return Optional.ofNullable(otherUserProfileImageUrl);
    }

    private BooleanExpression otherUserJoinCondition(UserId userId) {
        BooleanExpression ifUserIdSmall = conversationOneToOneKey.smallUserId.value.eq(userId.value());
        BooleanExpression ifUserIdLarge = conversationOneToOneKey.largeUserId.value.eq(userId.value());
        return ifUserIdSmall.and(user.id.eq(conversationOneToOneKey.largeUserId.value))
                .or(ifUserIdLarge.and(user.id.eq(conversationOneToOneKey.smallUserId.value)));
    }
}
