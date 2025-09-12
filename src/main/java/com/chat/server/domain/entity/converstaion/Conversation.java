package com.chat.server.domain.entity.converstaion;

import com.chat.server.common.code.ErrorCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.conversation.ConversationType;
import com.chat.server.common.exception.CustomException;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
import com.chat.server.domain.vo.ConversationId;
import com.chat.server.domain.vo.UserId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

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

    @Column(name = "IMAGE_URL", length = Constants.CONVERSATION_IMAGE_MAX_LENGTH)
    private String imageUrl;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CREATED_USER_ID", nullable = false))
    private UserId createdUserId;

    // If there is a value, the code must be correct
    // If hidden is True, the code is null
    @Column(name = "JOIN_CODE", length = 100)
    private String joinCode;

    // If true, this room is hidden room
    // If true, this room is Not searched for
    @Column(name = "IS_HIDDEN", nullable = false)
    private boolean hidden = false;

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
        conversation.createdUserId = creator.getUserId();
        conversation.joinCode = null;
        conversation.hidden = Boolean.TRUE;
        conversation.lastActivityAt = LocalDateTime.now();
        return conversation;
    }

    public static Conversation ofGroup(User creator,
                                       String title,
                                       String imageUrl,
                                       String joinCode,
                                       boolean hidden) {
        Conversation conversation = new Conversation();
        conversation.type = ConversationType.GROUP;
        conversation.title = title;
        conversation.imageUrl = StringUtils.hasText(imageUrl) ? imageUrl : null;
        conversation.createdUserId = creator.getUserId();
        conversation.joinCode = hidden ? null : joinCode;
        conversation.hidden = hidden;
        conversation.lastActivityAt = LocalDateTime.now();
        return conversation;
    }

    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    public ConversationId getConversationId() {
        return id == null ? null : ConversationId.of(id);
    }

    public boolean hasCode() {
        return joinCode != null && !joinCode.isBlank();
    }
}
