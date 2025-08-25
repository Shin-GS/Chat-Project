package com.chat.server.domain.entity.converstaion.participant;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.converstaion.Conversation;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CONVERSATION_PARTICIPANT_D",
        indexes = {
                @Index(name = "IX_CONVERSATION_PARTICIPANT_D_CONVERSATION", columnList = "USER_ID, CONVERSATION_ID"),
                @Index(name = "IX_CONVERSATION_PARTICIPANT_D_ROLE", columnList = "CONVERSATION_ID, ROLE")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CONVERSATION_PARTICIPANT_D_USER", columnNames = {"CONVERSATION_ID", "USER_ID"})
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class ConversationParticipant extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_PARTICIPANT_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "USER_ID", nullable = false))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 20, nullable = false)
    private ConversationUserRole role;

    @Column(name = "JOIN_MESSAGE_ID", nullable = false)
    private Long joinMessageId;

    @Column(name = "LAST_READ_MESSAGE_ID")
    private Long lastReadMessageId;

    @Column(name = "LAST_READ_AT")
    private LocalDateTime lastReadAt;

    @Column(name = "MUTED", nullable = false)
    private boolean muted = false;

    @Column(name = "JOIN_DTM", nullable = false)
    private LocalDateTime joinedAt;

    public static ConversationParticipant ofMember(Conversation conversation,
                                                   User user,
                                                   Long messageId) {
        ConversationParticipant participant = new ConversationParticipant();
        participant.conversationId = conversation.getConversationId();
        participant.userId = user.getUserId();
        participant.role = ConversationUserRole.MEMBER;
        participant.muted = false;
        participant.joinMessageId = messageId;
        participant.joinedAt = LocalDateTime.now();
        return participant;
    }

    public static ConversationParticipant ofSuperAdmin(Conversation conversation,
                                                       User user,
                                                       Long messageId) {
        ConversationParticipant participant = new ConversationParticipant();
        participant.conversationId = conversation.getConversationId();
        participant.userId = user.getUserId();
        participant.role = ConversationUserRole.SUPER_ADMIN;
        participant.muted = false;
        participant.joinMessageId = messageId;
        participant.joinedAt = LocalDateTime.now();
        return participant;
    }

    public void changeRole(ConversationUserRole newRole) {
        this.role = newRole;
    }

    public void updateJoinMessageId(Long messageId) {
        this.joinMessageId = messageId;
    }
}
