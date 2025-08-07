package com.chat.server.domain.repository;

import com.chat.server.domain.entity.ChatFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatFriendRepository extends JpaRepository<ChatFriend, Long> {
    List<ChatFriend> findAllByUserId(Long userId);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
