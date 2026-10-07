package com.SocialMediaApplication.SocialMediaApplication.controller;

import com.SocialMediaApplication.SocialMediaApplication.dto.ChatMessageDto;
import com.SocialMediaApplication.SocialMediaApplication.entity.Message;
import com.SocialMediaApplication.SocialMediaApplication.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessageDto dto, Principal principal) {
        String senderUsername = principal.getName();

        Message saved = chatService.saveMessage(
                senderUsername,
                dto.getReceiverUsername(),
                dto.getContent()
        );

        ChatMessageDto response = ChatMessageDto.from(saved);

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                dto.getReceiverUsername(),
                "/queue/messages",
                response
        );

        // Echo back to sender
        messagingTemplate.convertAndSendToUser(
                senderUsername,
                "/queue/messages",
                response
        );
    }
}