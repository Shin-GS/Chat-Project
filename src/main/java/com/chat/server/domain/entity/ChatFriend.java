package com.chat.server.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_friend")
public class ChatFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id")
    private Long fId;

    @Column
    private Long userId;

    @Column
    private Long friendId;

    public static ChatFriend of(Long userId,
                                Long friendId) {
        ChatFriend chatFriend = new ChatFriend();
        chatFriend.userId = userId;
        chatFriend.friendId = friendId;
        return chatFriend;
    }
}

