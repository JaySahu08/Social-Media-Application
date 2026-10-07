package com.SocialMediaApplication.SocialMediaApplication.controller;

import com.SocialMediaApplication.SocialMediaApplication.dto.ChatMessageDto;
import com.SocialMediaApplication.SocialMediaApplication.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatService chatService;

    @GetMapping("/history/{otherUsername}")
    public List<ChatMessageDto> history(@PathVariable String otherUsername,
                                        Authentication auth) {
        return chatService.getConversation(auth.getName(), otherUsername);
    }
}