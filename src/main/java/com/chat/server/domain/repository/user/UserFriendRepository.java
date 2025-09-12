package com.chat.server.domain.repository.user;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.user.UserFriend;
import com.chat.server.domain.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
    boolean existsByUserIdAndFriendUserId(UserId userId, UserId friendUserId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                        friendUser.id,
                        friendUser.accountId,
                        friendUser.username,
                        friendUser.profileImageUrl,
                        friendUser.statusMessage,
                        CASE WHEN (userFriend.id IS NOT NULL) THEN true ELSE false END
            )
            FROM UserFriend userFriend
            JOIN User friendUser ON userFriend.friendUserId.value = friendUser.id
            WHERE userFriend.userId.value = :#{#userId.value}
            ORDER BY friendUser.username
            """)
    List<UserDto> findAllByUserIdOrderByName(@Param("userId") UserId userId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                    friendUser.id,
                    friendUser.accountId,
                    friendUser.username,
                    friendUser.profileImageUrl,
                    friendUser.statusMessage,
                    CASE WHEN userFriend.id IS NOT NULL THEN true ELSE false END
                )
            FROM User friendUser
            LEFT JOIN UserFriend userFriend ON userFriend.friendUserId.value = friendUser.id AND userFriend.userId.value = :#{#userId.value}
            WHERE friendUser.id <> :#{#userId.value}
                AND (LOWER(friendUser.username) LIKE CONCAT('%', LOWER(:pattern), '%') OR LOWER(friendUser.accountId) LIKE CONCAT('%', LOWER(:pattern), '%'))
                ORDER BY LOWER(friendUser.username)
            """)
    List<UserDto> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern,
                                                      @Param("userId") UserId userId);

    void deleteByUserIdAndFriendUserId(UserId userId, UserId friendUserId);
}
