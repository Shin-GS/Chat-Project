package com.chat.server.domain.entity.user;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.UserRole;
import com.chat.server.domain.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USER_M",
        indexes = {
                @Index(name = "IX_USER_M_ACCOUNT_ID", columnList = "ACCOUNT_ID"),
                @Index(name = "IX_USER_M_USER_NAME", columnList = "USER_NAME")
        })
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(exclude = "hashedPassword")
public class User extends BaseTimeEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long id;

    @Column(name = "ACCOUNT_ID", length = Constants.USER_ID_MAX_LENGTH, nullable = false, unique = true)
    private String accountId;

    @Column(name = "HASHED_PASSWORD", length = Constants.USER_HASHED_PASSWORD_MAX_LENGTH, nullable = false)
    private String hashedPassword;

    @Column(name = "USER_NAME", length = Constants.USER_NAME_MAX_LENGTH, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", length = 50, nullable = false)
    private UserRole role = UserRole.USER;

    public static User ofUser(String accountId,
                              String hashedPassword,
                              String username) {
        User user = new User();
        user.accountId = accountId;
        user.hashedPassword = hashedPassword;
        user.username = username;
        user.role = UserRole.USER;
        return user;
    }
}
