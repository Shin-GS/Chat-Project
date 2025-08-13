package com.chat.server.domain.entity.converstaion;

import com.chat.server.common.constant.conversation.ConversationUserRole;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
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

    @Column(name = "CONVERSATION_ID", nullable = false)
    private Long conversationId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 20, nullable = false)
    private ConversationUserRole role;

    @Column(name = "LAST_READ_MESSAGE_ID")
    private Long lastReadMessageId;

    @Column(name = "LAST_READ_AT")
    private LocalDateTime lastReadAt;

    @Column(name = "MUTED", nullable = false)
    private boolean muted = false;

    @Column(name = "JOIN_DTM", nullable = false)
    private LocalDateTime joinedAt;

    public static ConversationParticipant of(Conversation conversation,
                                             User user,
                                             ConversationUserRole role) {
        ConversationParticipant p = new ConversationParticipant();
        p.conversationId = conversation.getId();
        p.userId = user.getId();
        p.role = role;
        p.muted = false;
        p.joinedAt = LocalDateTime.now();
        return p;
    }

    public void changeRole(ConversationUserRole newRole) {
        this.role = newRole;
    }
}
