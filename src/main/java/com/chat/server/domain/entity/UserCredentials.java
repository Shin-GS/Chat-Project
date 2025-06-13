package com.chat.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String hashed_password;

    public static UserCredentials of(User user, String hashed_password) {
        return UserCredentials.builder()
                .user(user)
                .hashed_password(hashed_password).build();
    }
}
