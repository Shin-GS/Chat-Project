package com.chat.server.domain.entity;

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
    private String sender;

    @Column
    private String receiver;

    @Column
    private String message;

    @Column
    private Timestamp createdAt;

    public static Chat of(String sender,
                          String receiver,
                          String message) {
        Chat chat = new Chat();
        chat.sender = sender;
        chat.receiver = receiver;
        chat.message = message;
        chat.createdAt = new Timestamp(System.currentTimeMillis());
        return chat;
    }
}
