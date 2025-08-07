package com.chat.server.domain.repository;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                    user.id,
                    user.username,
                    CASE WHEN chatFriend.fId IS NOT NULL THEN true ELSE false END
                )
                FROM User user
                LEFT JOIN ChatFriend chatFriend ON chatFriend.friendUserId = user.id AND chatFriend.userId = :userId
                WHERE LOCATE(LOWER(:pattern), LOWER(user.username)) > 0
                      AND user.id != :userId
                ORDER BY user.username
            """)
    List<UserDto> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern,
                                                      @Param("userId") Long userId);
} 
