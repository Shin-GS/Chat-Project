package com.chat.server.domain.repository;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.ChatFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long> {
    boolean existsByUserIdAndFriendUserId(Long userId, Long friendUserId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                        user.id,
                        user.username,
                       CASE WHEN (chatFriend.fId IS NOT NULL) THEN true ELSE false END
            )
            FROM ChatFriend chatFriend
            JOIN User user ON chatFriend.friendUserId = user.id
            WHERE chatFriend.userId = :userId
            ORDER BY user.username
            """)
    List<UserDto> findAllByUserIdOrderByName(@Param("userId") Long userId);
}
