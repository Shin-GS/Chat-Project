package com.chat.server.domain.entity;

import com.chat.server.common.constant.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredentials userCredentials;

    public static User of(String username,
                          String hashedPassword) {
        User user = new User();
        user.username = username;
        user.userCredentials = UserCredentials.of(user, hashedPassword);
        user.role = MemberRole.USER;
        user.createdAt = new Timestamp(System.currentTimeMillis());
        return user;
    }

    public void setCredentials(UserCredentials credentials) {
        this.userCredentials = credentials;
    }
}
