package com.chat.server.domain.repository.conversation.query.impl;

import com.chat.server.domain.dto.ConversationParticipantDto;
import com.chat.server.domain.repository.conversation.query.ConversationParticipantQueryRepository;
import com.chat.server.domain.vo.ConversationId;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.chat.server.domain.entity.converstaion.participant.QConversationParticipant.conversationParticipant;
import static com.chat.server.domain.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ConversationParticipantQueryRepositoryImpl implements ConversationParticipantQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConversationParticipantDto> findDtoAllByConversationId(ConversationId conversationId) {
        return queryFactory
                .select(Projections.constructor(
                        ConversationParticipantDto.class,
                        conversationParticipant.userId,
                        user.username,
                        conversationParticipant.role))
                .from(conversationParticipant)
                .join(user).on(user.id.eq(conversationParticipant.userId.value))
                .where(conversationParticipant.conversationId.value.eq(conversationId.value()))
                .fetch();
    }
}
