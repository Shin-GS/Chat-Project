package com.chat.server.domain.entity.chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CHAT_FRIEND")
public class ChatFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_FRIEND_ID", columnDefinition = "BIGINT")
    private Long chatFriendId;

    @Column(name = "USER_ID", columnDefinition = "BIGINT")
    private Long userId;

    @Column(name = "FRIEND_USER_ID", columnDefinition = "BIGINT")
    private Long friendUserId;

    public static ChatFriend of(Long userId,
                                Long friendUserId) {
        ChatFriend chatFriend = new ChatFriend();
        chatFriend.userId = userId;
        chatFriend.friendUserId = friendUserId;
        return chatFriend;
    }
}
