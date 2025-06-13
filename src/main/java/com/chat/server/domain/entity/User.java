package com.chat.server.domain.entity;

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
    private String name;

    @Column
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserCredentials userCredentials;

    public static User of(String name,
                          String hashedPassword) {
        User user = new User();
        user.name = name;
        user.userCredentials = UserCredentials.of(user, hashedPassword);
        user.createdAt = new Timestamp(System.currentTimeMillis());
        return user;
    }

    public void setCredentials(UserCredentials credentials) {
        this.userCredentials = credentials;
    }
}
