package com.SocialMediaApplication.SocialMediaApplication.repository;

import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
        SELECT u FROM User u
        WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')))
          AND u.username <> :currentUsername
        """)
    List<User> searchUsers(@Param("query") String query,
                           @Param("currentUsername") String currentUsername);
}