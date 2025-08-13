package com.chat.server.domain.entity.chat;

import com.chat.server.common.constant.Constants;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "CHAT_MESSAGE_D",
        indexes = {
                @Index(name = "IX_CHAT_M_PAIR_A", columnList = "SENDER_USER_ID, RECEIVER_USER_ID, CHAT_ID"), // todo ChatRoom 도입시 수정 필요
                @Index(name = "IX_CHAT_M_PAIR_B", columnList = "RECEIVER_USER_ID, SENDER_USER_ID, CHAT_ID")
        }
)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "message")
public class ChatMessage extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ID")
    private Long id;

    @Column(name = "SENDER_USER_ID", nullable = false)
    private Long senderUserId;

    @Column(name = "RECEIVER_USER_ID", nullable = false)
    private Long receiverUserId;

    @Column(name = "SENDER_USER_NAME", length = Constants.USER_NAME_MAX_LENGTH, nullable = false)
    private String senderUsername;

    @Column(name = "RECEIVER_USER_NAME", length = Constants.USER_NAME_MAX_LENGTH, nullable = false)
    private String receiverUsername;

    @Column(name = "MESSAGE", length = Constants.CONVERSATION_MESSAGE_MAX_LENGTH, nullable = false)
    private String message;

    public static ChatMessage of(User sender,
                                 User receiver,
                                 String message) {
        ChatMessage chat = new ChatMessage();
        chat.senderUserId = sender.getId();
        chat.senderUsername = sender.getUsername();
        chat.receiverUserId = receiver.getId();
        chat.receiverUsername = receiver.getUsername();
        chat.message = message;
        return chat;
    }
}
