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
    List<ChatFriend> findAllByUserId(Long userId);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(user.id, user.username)
            FROM ChatFriend chatFriend
            JOIN User user ON chatFriend.friendId = user.id
            WHERE chatFriend.userId = :userId
            ORDER BY user.username
            """)
    List<UserDto> findAllByUserIdOrderByName(@Param("userId") Long userId);
}
