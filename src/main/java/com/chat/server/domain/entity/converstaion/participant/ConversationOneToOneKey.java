package com.chat.server.domain.entity.converstaion.participant;

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
@Table(name = "CONVERSATION_ONE_TO_ONE_KEY_M",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CONVERSATION_ONE_TO_ONE_KEY_M_PAIR", columnNames = {"SMALL_USER_ID", "LARGE_USER_ID"}),
                @UniqueConstraint(name = "UK_CONVERSATION_ONE_TO_ONE_KEY_M_CONV", columnNames = {"CONVERSATION_ID"})
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class ConversationOneToOneKey extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ONE_TO_ONE_KEY_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "CONVERSATION_ID", nullable = false))
    private ConversationId conversationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "SMALL_USER_ID", nullable = false))
    private UserId smallUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "LARGE_USER_ID", nullable = false))
    private UserId largeUserId;

    public static ConversationOneToOneKey of(Conversation conversation,
                                             User userA,
                                             User userB) {
        Long userAId = userA.getId();
        Long userBId = userB.getId();

        ConversationOneToOneKey oneToOneKey = new ConversationOneToOneKey();
        oneToOneKey.conversationId = conversation.getConversationId();
        oneToOneKey.smallUserId = UserId.of(Math.min(userAId, userBId));
        oneToOneKey.largeUserId = UserId.of(Math.max(userAId, userBId));
        return oneToOneKey;
    }
}
