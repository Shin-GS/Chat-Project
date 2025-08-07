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
            SELECT new com.chat.server.domain.dto.UserDto(user.id, user.username)
            FROM User AS user
            WHERE LOCATE(LOWER(:pattern), LOWER(user.username)) > 0 AND user.username != :username
            """)
    List<UserDto> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern, @Param("username") String username);
} 
