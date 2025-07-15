package com.chat.server.domain.repository;

import com.chat.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT user.name FROM User AS user WHERE LOCATE(LOWER(:pattern), LOWER(user.name)) > 0 AND user.name != :username")
    List<String> findSimilarNamesExcludingExactMatch(@Param("pattern") String pattern, @Param("username") String username);
} 
