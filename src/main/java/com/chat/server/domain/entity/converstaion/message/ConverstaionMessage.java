package com.chat.server.domain.entity.converstaion.message;

import com.chat.server.common.constant.Constants;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "CONVERSATION_MESSAGE_D",
        indexes = {
                @Index(name = "IX_CONVERSATION_MESSAGE_D_PAIR_A", columnList = "SENDER_USER_ID, RECEIVER_USER_ID, MESSAGE_ID"),
                @Index(name = "IX_CONVERSATION_MESSAGE_D_PAIR_B", columnList = "RECEIVER_USER_ID, SENDER_USER_ID, MESSAGE_ID")
        }
)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "message")
public class ConverstaionMessage extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
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

    public static ConverstaionMessage of(User sender,
                                         User receiver,
                                         String message) {
        ConverstaionMessage converstaionMessage = new ConverstaionMessage();
        converstaionMessage.senderUserId = sender.getId();
        converstaionMessage.senderUsername = sender.getUsername();
        converstaionMessage.receiverUserId = receiver.getId();
        converstaionMessage.receiverUsername = receiver.getUsername();
        converstaionMessage.message = message;
        return converstaionMessage;
    }
}
