package com.chat.server.domain.entity.converstaion.history;

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
@Table(name = "CONVERSATION_ROLE_H",
        indexes = {
                @Index(name = "IX_CONVERSATION_ROLE_H_CONVERSATION_USER_AT", columnList = "CONVERSATION_ID, USER_ID, CHANGED_AT"),
                @Index(name = "IX_CONVERSATION_ROLE_H_ACTOR_AT", columnList = "ACTOR_USER_ID, CHANGED_AT")
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class ConversationRoleHistory extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_ROLE_H_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "USER_ID", nullable = false))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_OLD", length = 20)
    private ConversationUserRole roleOld;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NEW", length = 20)
    private ConversationUserRole roleNew;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ACTOR_USER_ID", nullable = false))
    private UserId actorUserId; // who changed the role

    @Column(name = "CHANGED_AT", nullable = false)
    private LocalDateTime changedAt;

    public static ConversationRoleHistory ofNew(Conversation conversation,
                                                User user,
                                                ConversationUserRole newRole,
                                                User actorUser) {
        ConversationRoleHistory history = new ConversationRoleHistory();
        history.conversationId = conversation.getConversationId();
        history.userId = user.getUserId();
        history.roleOld = null;
        history.roleNew = newRole;
        history.actorUserId = actorUser == null ? user.getUserId() : actorUser.getUserId();
        history.changedAt = LocalDateTime.now();
        return history;
    }

    public static ConversationRoleHistory ofChange(Conversation conversation,
                                                   User user,
                                                   ConversationUserRole oldRole,
                                                   ConversationUserRole newRole,
                                                   User actorUser) {
        ConversationRoleHistory history = new ConversationRoleHistory();
        history.conversationId = conversation.getConversationId();
        history.userId = user.getUserId();
        history.roleOld = oldRole;
        history.roleNew = newRole;
        history.actorUserId = actorUser == null ? user.getUserId() : actorUser.getUserId();
        history.changedAt = LocalDateTime.now();
        return history;
    }

    public static ConversationRoleHistory ofLeave(Conversation conversation,
                                                  User user,
                                                  ConversationUserRole oldRole,
                                                  User actorUser) {
        ConversationRoleHistory history = new ConversationRoleHistory();
        history.conversationId = conversation.getConversationId();
        history.userId = user.getUserId();
        history.roleOld = oldRole;
        history.roleNew = null;
        history.actorUserId = actorUser == null ? user.getUserId() : actorUser.getUserId();
        history.changedAt = LocalDateTime.now();
        return history;
    }
}
