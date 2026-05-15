package com.jay.SocialMedia.Repository;

import com.jay.SocialMedia.Entity.ChatMessage;
import com.jay.SocialMedia.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtAsc(
            User sender,
            User receiver,
            User receiverMirror,
            User senderMirror
    );
}
