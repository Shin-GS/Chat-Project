package com.chat.server.domain.entity.converstaion;

import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.entity.user.User;
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

    @Column(name = "CONVERSATION_ID", nullable = false)
    private Long conversationId;

    @Column(name = "SMALL_USER_ID", nullable = false)
    private Long smallUserId;

    @Column(name = "LARGE_USER_ID", nullable = false)
    private Long largeUserId;

    public static ConversationOneToOneKey of(Conversation conversation,
                                             User userA,
                                             User userB) {
        Long userAId = userA.getId();
        Long userBId = userB.getId();

        ConversationOneToOneKey oneToOneKey = new ConversationOneToOneKey();
        oneToOneKey.conversationId = conversation.getId();
        oneToOneKey.smallUserId = Math.min(userAId, userBId);
        oneToOneKey.largeUserId = Math.max(userAId, userBId);
        return oneToOneKey;
    }
}
