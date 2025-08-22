package com.chat.server.domain.entity.converstaion.history;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationMembershipAction;
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
@Table(name = "CONVERSATION_MEMBERSHIP_H",
        indexes = {
                @Index(name = "IX_CONVERSATION_MEMBERSHIP_H_CONVERSATION_USER_AT", columnList = "CONVERSATION_ID, USER_ID, ACTION_AT"),
                @Index(name = "IX_CONVERSATION_MEMBERSHIP_H_ACTOR_AT", columnList = "ACTOR_USER_ID, ACTION_AT")
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class ConversationMembershipHistory extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_MEMBERSHIP_H_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "USER_ID", nullable = false))
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION", length = 20, nullable = false)
    private ConversationMembershipAction action;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ACTOR_USER_ID", nullable = false))
    private UserId actorUserId; // self or admin

    @Column(name = "REASON", length = Constants.CONVERSATION_MEMBERSHIP_REASON_MAX_LENGTH)
    private String reason;

    @Column(name = "ACTION_AT", nullable = false)
    private LocalDateTime actionAt;

    public static ConversationMembershipHistory ofJoin(Conversation conversation,
                                                       User user,
                                                       User actorUser) {
        ConversationMembershipHistory history = new ConversationMembershipHistory();
        history.conversationId = conversation.getConversationId();
        history.userId = user.getUserId();
        history.action = ConversationMembershipAction.JOIN;
        history.actorUserId = actorUser == null ? user.getUserId() : actorUser.getUserId();
        history.actionAt = LocalDateTime.now();
        return history;
    }

    public static ConversationMembershipHistory ofLeave(Conversation conversation,
                                                        User user,
                                                        User actorUser) {
        ConversationMembershipHistory history = new ConversationMembershipHistory();
        history.conversationId = conversation.getConversationId();
        history.userId = user.getUserId();
        history.action = ConversationMembershipAction.LEAVE;
        history.actorUserId = actorUser == null ? user.getUserId() : actorUser.getUserId();
        history.actionAt = LocalDateTime.now();
        return history;
    }
}
