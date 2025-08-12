package com.chat.server.domain.entity.chat;

import com.chat.server.common.constant.chat.ChatType;
import com.chat.server.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CHAT_ROOM_M")
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ROOM_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ChatType type;

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    private Long createUserId;

    private String createUsername;

    private LocalDateTime lastModifiedAt;
}
