package com.chat.server.domain.entity.converstaion.message;

import com.chat.server.common.constant.Constants;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "CONVERSATION_MESSAGE_D",
        indexes = {
                @Index(name = "IX_CONVERSATION_MESSAGE_D_ORDER", columnList = "CONVERSATION_ID, MESSAGE_ID"),
                @Index(name = "IX_CONVERSATION_MESSAGE_D_SENDER", columnList = "SENDER_USER_ID, CONVERSATION_ID, MESSAGE_ID")
        }
)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class ConversationMessage extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "SENDER_USER_ID", nullable = false))
    private UserId senderUserId;

    @Column(name = "SENDER_USER_NAME", length = Constants.USER_NAME_MAX_LENGTH, nullable = false)
    private String senderUsername;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Column(name = "MESSAGE", length = Constants.CONVERSATION_MESSAGE_MAX_LENGTH, nullable = false)
    private String message;

    public static ConversationMessage of(User sender,
                                         Conversation conversation,
                                         String message) {
        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.senderUserId = sender.getUserId();
        conversationMessage.senderUsername = sender.getUsername();
        conversationMessage.conversationId = conversation.getConversationId();
        conversationMessage.message = message;
        return conversationMessage;
    }
}
