package com.chat.server.domain.entity.converstaion;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CONVERSATION_M",
        indexes = {
                @Index(name = "IX_CONVERSATION_M_TYPE", columnList = "TYPE"),
                @Index(name = "IX_CONVERSATION_M_LAST_MOD_DTM", columnList = "LAST_ACTIVITY_DTM")
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class Conversation extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50, nullable = false)
    private ConversationType type;

    @Column(name = "NAME", length = Constants.CONVERSATION_NAME_MAX_LENGTH)
    private String name;

    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;

    @Column(name = "LAST_ACTIVITY_DTM", nullable = false)
    private LocalDateTime lastActivityAt;

    public static Conversation ofOneToOne(User user) {
        Conversation conversation = new Conversation();
        conversation.type = ConversationType.ONE_TO_ONE;
        conversation.createdUserId = user.getId();
        conversation.lastActivityAt = LocalDateTime.now();
        return conversation;
    }

    public static Conversation ofGroup(User user, String name) {
        Conversation conversation = new Conversation();
        conversation.type = ConversationType.GROUP;
        conversation.name = name;
        conversation.createdUserId = user.getId();
        conversation.lastActivityAt = LocalDateTime.now();
        return conversation;
    }

    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }
}
