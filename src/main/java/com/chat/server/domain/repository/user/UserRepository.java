package com.chat.server.domain.repository.user;

import com.chat.server.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);
} 
