package com.chat.server.domain.repository.user;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.user.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
    boolean existsByUserIdAndFriendUserId(Long userId, Long friendUserId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                        user.id,
                        user.accountId,
                        user.username,
                        CASE WHEN (userFriend.id IS NOT NULL) THEN true ELSE false END
            )
            FROM UserFriend userFriend
            JOIN User user ON userFriend.friendUserId = user.id
            WHERE userFriend.userId = :userId
            ORDER BY user.username
            """)
    List<UserDto> findAllByUserIdOrderByName(@Param("userId") Long userId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                    user.id,
                    user.accountId,
                    user.username,
                    CASE WHEN userFriend.id IS NOT NULL THEN true ELSE false END
                )
            FROM User user
            LEFT JOIN UserFriend userFriend ON userFriend.friendUserId = user.id AND userFriend.userId = :userId
            WHERE user.id <> :userId
                AND (LOWER(user.username) LIKE CONCAT('%', LOWER(:pattern), '%') OR LOWER(user.accountId) LIKE CONCAT('%', LOWER(:pattern), '%'))
                ORDER BY LOWER(user.username)
            """)
    List<UserDto> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern,
                                                      @Param("userId") Long userId);

    void deleteByUserIdAndFriendUserId(Long userId, Long friendUserId);
}
