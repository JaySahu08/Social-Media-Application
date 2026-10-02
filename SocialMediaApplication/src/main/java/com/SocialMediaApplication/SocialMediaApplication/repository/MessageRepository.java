package com.SocialMediaApplication.SocialMediaApplication.repository;

import com.SocialMediaApplication.SocialMediaApplication.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.username = :u1 AND m.receiver.username = :u2)
           OR (m.sender.username = :u2 AND m.receiver.username = :u1)
        ORDER BY m.sentAt ASC
        """)
    List<Message> findConversation(@Param("u1") String u1, @Param("u2") String u2);
}