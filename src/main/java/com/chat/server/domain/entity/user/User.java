package com.chat.server.domain.entity.user;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.constant.UserRole;
import com.chat.server.domain.entity.BaseTimeEntity;
import com.chat.server.domain.vo.UserId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

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
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "ACCOUNT_ID", length = Constants.USER_ID_MAX_LENGTH, nullable = false, unique = true)
    private String accountId;

    @Column(name = "HASHED_PASSWORD", length = Constants.USER_HASHED_PASSWORD_MAX_LENGTH, nullable = false)
    private String hashedPassword;

    @Column(name = "USER_NAME", length = Constants.USER_NAME_MAX_LENGTH, nullable = false)
    private String username;

    @Column(name = "PROFILE_IMAGE_URL", length = Constants.USER_IMAGE_MAX_LENGTH)
    private String profileImageUrl;

    @Column(name = "STATUS_MESSAGE", length = Constants.USER_STATUS_MESSAGE_MAX_LENGTH)
    private String statusMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", length = Constants.USER_ROLE_MAX_LENGTH, nullable = false)
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

    public UserId getUserId() {
        return id == null ? null : UserId.of(id);
    }

    public void updateProfile(String username,
                              String profileImageUrl,
                              String statusMessage) {
        // updated only if text is present (not null/blank).
        if (StringUtils.hasText(username)) {
            this.username = username;
        }

        // nullable, will overwrite with given value (can be null).
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }

        // nullable, will overwrite with given value (can be null).
        if (statusMessage != null) {
            this.statusMessage = statusMessage;
        }
    }
}
