package com.SocialMediaApplication.SocialMediaApplication.repository;

import com.SocialMediaApplication.SocialMediaApplication.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender s
        JOIN FETCH m.receiver r
        WHERE (s.username = :u1 AND r.username = :u2)
           OR (s.username = :u2 AND r.username = :u1)
        ORDER BY m.sentAt ASC
        """)
    List<Message> findConversation(@Param("u1") String u1, @Param("u2") String u2);

    @Query("""
        SELECT COUNT(u) > 0 FROM User u
        JOIN u.friends f
        WHERE u.username = :senderUsername AND f.username = :receiverUsername
        """)
    boolean areFriends(@Param("senderUsername") String senderUsername,
                       @Param("receiverUsername") String receiverUsername);
}