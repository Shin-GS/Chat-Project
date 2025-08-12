package com.chat.server.domain.entity;

import com.chat.server.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id")
    private Long tId;

    @Column
    private Long senderUserId;

    @Column
    private String sender;

    @Column
    private String receiver;

    @Column
    private Long receiverUserId;

    @Column
    private String message;

    @Column
    private Timestamp createdAt;

    public static Chat of(User sender,
                          User receiver,
                          String message) {
        Chat chat = new Chat();
        chat.senderUserId = sender.getId();
        chat.sender = sender.getUsername();
        chat.receiverUserId = receiver.getId();
        chat.receiver = receiver.getUsername();
        chat.message = message;
        chat.createdAt = new Timestamp(System.currentTimeMillis());
        return chat;
    }
}
