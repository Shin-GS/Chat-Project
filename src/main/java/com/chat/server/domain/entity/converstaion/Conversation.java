package com.chat.server.domain.entity.converstaion;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.exception.CustomException;
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

    @Column(name = "TITLE", length = Constants.CONVERSATION_TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;

    @Column(name = "JOIN_CODE", length = 100)
    private String joinCode; // If there is a value, the code must be correct

    @Column(name = "IS_HIDDEN", nullable = false)
    private boolean hidden = false; // If true, by invitation only

    @Column(name = "LAST_ACTIVITY_DTM", nullable = false)
    private LocalDateTime lastActivityAt;

    // Delete Prevention
    @PreRemove
    private void preventDelete() {
        throw new CustomException(ErrorCode.ENTITY_DELETE_FORBIDDEN);
    }

    public static Conversation ofOneToOne(User creator) {
        Conversation conversation = new Conversation();
        conversation.type = ConversationType.ONE_TO_ONE;
        conversation.createdUserId = creator.getId();
        conversation.lastActivityAt = LocalDateTime.now();
        return conversation;
    }

    public static Conversation ofGroup(User creator,
                                       String name,
                                       String joinCode,
                                       boolean hidden) {
        Conversation conversation = new Conversation();
        conversation.type = ConversationType.GROUP;
        conversation.title = name;
        conversation.createdUserId = creator.getId();
        conversation.lastActivityAt = LocalDateTime.now();
        conversation.joinCode = joinCode;
        conversation.hidden = hidden;
        return conversation;
    }

    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }
}
