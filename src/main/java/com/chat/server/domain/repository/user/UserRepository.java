package com.chat.server.domain.repository.user;

import com.chat.server.domain.dto.UserDto;
import com.chat.server.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

    @Query("""
            SELECT new com.chat.server.domain.dto.UserDto(
                    user.id,
                    user.username,
                    CASE WHEN userFriend.id IS NOT NULL THEN true ELSE false END
                )
                FROM User user
                LEFT JOIN UserFriend userFriend ON userFriend.friendUserId = user.id AND userFriend.userId = :userId
                WHERE LOCATE(LOWER(:pattern), LOWER(user.username)) > 0
                      AND user.id != :userId
                ORDER BY user.username
            """)
    List<UserDto> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern,
                                                      @Param("accountId") Long userId);
} 
