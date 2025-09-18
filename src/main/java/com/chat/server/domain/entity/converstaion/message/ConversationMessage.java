package com.chat.server.domain.entity.converstaion.message;

import com.chat.server.common.constant.conversation.ConversationMessageType;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.converstaion.sticker.Sticker;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import jakarta.persistence.*;
import lombok.*;

import static com.chat.server.common.constant.Constants.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "CONVERSATION_MESSAGE_D",
        indexes = {
                @Index(name = "IX_CONVERSATION_MESSAGE_D_ORDER", columnList = "CONVERSATION_ID, MESSAGE_ID"),
                @Index(name = "IX_CONVERSATION_MESSAGE_D_SENDER", columnList = "SENDER_USER_ID, CONVERSATION_ID, MESSAGE_ID"),
                @Index(name = "IX_CONVERSATION_MESSAGE_D_STICKER", columnList = "STICKER_ID")
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

    @Column(name = "SENDER_USER_NAME", length = USER_NAME_MAX_LENGTH, nullable = false)
    private String senderUsername;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = CONVERSATION_TYPE_MAX_LENGTH, nullable = false)
    private ConversationMessageType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Column(name = "MESSAGE", length = CONVERSATION_MESSAGE_MAX_LENGTH, nullable = false)
    private String message;

    @Column(name = "STICKER_ID")
    private Long stickerId;

    public static ConversationMessage ofText(User sender,
                                             Conversation conversation,
                                             String message) {
        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.senderUserId = sender.getUserId();
        conversationMessage.senderUsername = sender.getUsername();
        conversationMessage.conversationId = conversation.getConversationId();
        conversationMessage.type = ConversationMessageType.TEXT;
        conversationMessage.message = message;
        return conversationMessage;
    }

    public static ConversationMessage ofSystem(User sender,
                                               Conversation conversation,
                                               String message) {
        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.senderUserId = sender.getUserId();
        conversationMessage.senderUsername = sender.getUsername();
        conversationMessage.conversationId = conversation.getConversationId();
        conversationMessage.type = ConversationMessageType.SYSTEM;
        conversationMessage.message = message;
        return conversationMessage;
    }

    public static ConversationMessage ofSticker(User sender,
                                                Conversation conversation,
                                                Sticker sticker) {
        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.senderUserId = sender.getUserId();
        conversationMessage.senderUsername = sender.getUsername();
        conversationMessage.conversationId = conversation.getConversationId();
        conversationMessage.type = ConversationMessageType.STICKER;
        conversationMessage.message = sticker.getAltText() == null ? "" : sticker.getAltText();
        conversationMessage.stickerId = sticker.getId();
        return conversationMessage;
    }
}
