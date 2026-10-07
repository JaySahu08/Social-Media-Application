package com.SocialMediaApplication.SocialMediaApplication.service;

import com.SocialMediaApplication.SocialMediaApplication.dto.ChatMessageDto;
import com.SocialMediaApplication.SocialMediaApplication.entity.Message;
import com.SocialMediaApplication.SocialMediaApplication.entity.User;
import com.SocialMediaApplication.SocialMediaApplication.exception.BadRequestException;
import com.SocialMediaApplication.SocialMediaApplication.exception.ResourceNotFoundException;
import com.SocialMediaApplication.SocialMediaApplication.repository.MessageRepository;
import com.SocialMediaApplication.SocialMediaApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public Message saveMessage(String senderUsername, String receiverUsername, String content) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        if (!messageRepository.areFriends(senderUsername, receiverUsername)) {
            throw new BadRequestException("You can only chat with friends");
        }

        return messageRepository.save(Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getConversation(String user1, String user2) {
        return messageRepository.findConversation(user1, user2)
                .stream()
                .map(ChatMessageDto::from)
                .toList();
    }
}