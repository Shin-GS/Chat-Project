package com.chat.server.domain.repository.user.query.impl;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.repository.user.query.UserFriendQueryRepository;
import com.chat.server.domain.vo.UserId;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.chat.server.common.constant.Constants.*;
import static com.chat.server.domain.entity.user.QUser.user;
import static com.chat.server.domain.entity.user.QUserFriend.userFriend;

@Repository
@RequiredArgsConstructor
public class UserFriendQueryRepositoryImpl implements UserFriendQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserDto> findAllByUserId(UserId userId,
                                         String order,
                                         SortDirection direction) {
        return queryFactory
                .select(Projections.constructor(
                        UserDto.class,
                        user.id,
                        user.accountId,
                        user.username,
                        user.profileImageUrl,
                        user.statusMessage,
                        userFriend.id.isNotNull()
                ))
                .from(userFriend)
                .join(user).on(userFriend.friendUserId.value.eq(user.id))
                .where(userFriend.userId.value.eq(userId.value()))
                .orderBy(orderSpecifiers(order, direction))
                .fetch();
    }

    @Override
    public List<UserDto> findSimilarNamesExcludingExactMatch(String pattern,
                                                             UserId userId,
                                                             String order,
                                                             SortDirection direction) {
        return queryFactory
                .select(Projections.constructor(
                        UserDto.class,
                        user.id,
                        user.accountId,
                        user.username,
                        user.profileImageUrl,
                        user.statusMessage,
                        userFriend.id.isNotNull()
                ))
                .from(user)
                .leftJoin(userFriend).on(userFriend.friendUserId.value.eq(user.id).and(userFriend.userId.value.eq(userId.value())))
                .where(user.id.ne(userId.value()),
                        user.username.containsIgnoreCase(pattern).or(user.accountId.containsIgnoreCase(pattern)))
                .orderBy(orderSpecifiers(order, direction))
                .fetch();
    }

    private OrderSpecifier<?> orderSpecifiers(String order,
                                              SortDirection direction) {
        return switch (order) {
            case ORDER_USER_ID -> switch (direction) {
                case ASCENDING -> user.id.asc();
                case DESCENDING -> user.id.desc();
            };
            case ORDER_USER_FRIEND_ID -> switch (direction) {
                case ASCENDING -> userFriend.id.asc();
                case DESCENDING -> userFriend.id.desc();
            };
            case ORDER_USER_NAME -> switch (direction) {
                case ASCENDING -> user.username.lower().asc();
                case DESCENDING -> user.username.lower().desc();
            };
            default -> userFriend.id.asc();
        };
    }
}
