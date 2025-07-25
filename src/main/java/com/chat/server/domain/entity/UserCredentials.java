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
    private String hashedPassword;

    public static UserCredentials of(User user, String hashedPassword) {
        return UserCredentials.builder()
                .user(user)
                .hashedPassword(hashedPassword).build();
    }
}
