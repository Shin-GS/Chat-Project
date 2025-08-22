package com.chat.server.domain.entity.user;

import com.chat.server.domain.vo.UserId;
import com.chat.server.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "USER_FRIEND_D",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER_FRIEND_D_PAIR", columnNames = {"USER_ID", "FRIEND_USER_ID"})
        },
        indexes = {
                @Index(name = "IX_USER_FRIEND_D_USER", columnList = "USER_ID"),
                @Index(name = "IX_USER_FRIEND_D_FRIEND", columnList = "FRIEND_USER_ID")
        }
)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString
public class UserFriend extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_FRIEND_ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "USER_ID", nullable = false))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "FRIEND_USER_ID", nullable = false))
    private UserId friendUserId;

    public static UserFriend of(UserId userId,
                                UserId friendUserId) {
        UserFriend userFriend = new UserFriend();
        userFriend.userId = userId;
        userFriend.friendUserId = friendUserId;
        return userFriend;
    }
}
