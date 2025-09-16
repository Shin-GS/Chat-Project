package com.chat.server.domain.repository.conversation.query.impl;

import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.dto.ConversationDto;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.repository.conversation.query.ConversationQueryRepository;
import com.chat.server.domain.vo.UserId;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.chat.server.common.constant.Constants.ORDER_CONVERSATION_ACTIVITY_AT;
import static com.chat.server.common.constant.Constants.ORDER_CONVERSATION_ID;
import static com.chat.server.domain.entity.converstaion.QConversation.conversation;
import static com.chat.server.domain.entity.converstaion.participant.QConversationOneToOneKey.conversationOneToOneKey;
import static com.chat.server.domain.entity.converstaion.participant.QConversationParticipant.conversationParticipant;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

@Repository
@RequiredArgsConstructor
public class ConversationQueryRepositoryImpl implements ConversationQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Conversation> findAllBy(UserId userId,
                                        String order,
                                        SortDirection direction) {
        BooleanExpression existsParticipant = JPAExpressions.selectOne()
                .from(conversationParticipant)
                .where(conversationParticipant.conversationId.value.eq(conversation.id)
                        .and(conversationParticipant.userId.value.eq(userId.value())))
                .exists();

        return queryFactory
                .selectFrom(conversation)
                .where(existsParticipant)
                .orderBy(orderSpecifiers(order, direction))
                .fetch();
    }

    @Override
    public Optional<Conversation> findOneToOneConversation(UserId smallUserId,
                                                           UserId largeUserId) {
        Conversation fetchConversation = queryFactory
                .select(conversation)
                .from(conversationOneToOneKey)
                .join(conversation).on(conversation.id.eq(conversationOneToOneKey.conversationId.value))
                .where(conversationOneToOneKey.smallUserId.value.eq(smallUserId.value()),
                        conversationOneToOneKey.largeUserId.value.eq(largeUserId.value()))
                .fetchOne();
        return Optional.ofNullable(fetchConversation);
    }

    @Override
    public boolean existsJoinAbleGroups(UserId userId, String keyword) {
        return queryFactory
                .selectOne()
                .from(conversation)
                .where(visibleGroup(),
                        containsTitleIfKeywordPresent(keyword),
                        notJoinedBy(userId))
                .fetchFirst() != null;
    }

    @Override
    public Page<ConversationDto> searchJoinAbleGroups(UserId userId,
                                                      String keyword,
                                                      Pageable pageable) {
        List<ConversationDto> content = queryFactory
                .select(Projections.constructor(
                        ConversationDto.class,
                        conversation.id,
                        conversation.type,
                        new CaseBuilder()
                                .when(conversation.title.isNull()).then("Untitled group")
                                .otherwise(conversation.title),
                        numberTemplate(Integer.class, "length(trim(coalesce({0}, '')))", conversation.joinCode).gt(0),
                        conversation.lastActivityAt))
                .from(conversation)
                .where(visibleGroup(),
                        containsTitleIfKeywordPresent(keyword),
                        notJoinedBy(userId))
                .orderBy(orderSpecifiers(ORDER_CONVERSATION_ACTIVITY_AT, SortDirection.DESCENDING),
                        orderSpecifiers(ORDER_CONVERSATION_ID, SortDirection.DESCENDING))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        var countQuery = queryFactory
                .select(conversation.id.count())
                .from(conversation)
                .where(visibleGroup(),
                        containsTitleIfKeywordPresent(keyword),
                        notJoinedBy(userId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private static BooleanExpression visibleGroup() {
        return conversation.type.eq(ConversationType.GROUP).and(conversation.hidden.isFalse());
    }

    private BooleanExpression containsTitleIfKeywordPresent(String keyword) {
        return StringUtils.hasText(keyword) ? conversation.title.containsIgnoreCase(keyword) : null;
    }

    private static BooleanExpression notJoinedBy(UserId userId) {
        return JPAExpressions.selectOne()
                .from(conversationParticipant)
                .where(conversationParticipant.conversationId.value.eq(conversation.id)
                        .and(conversationParticipant.userId.value.eq(userId.value())))
                .notExists();
    }

    private OrderSpecifier<?> orderSpecifiers(String order,
                                              SortDirection direction) {
        return switch (order) {
            case ORDER_CONVERSATION_ID -> switch (direction) {
                case ASCENDING -> conversation.id.asc();
                case DESCENDING -> conversation.id.desc();
            };
            case ORDER_CONVERSATION_ACTIVITY_AT -> switch (direction) {
                case ASCENDING -> conversation.lastActivityAt.asc();
                case DESCENDING -> conversation.lastActivityAt.desc();
            };
            default -> conversation.id.asc();
        };
    }
}
