package com.jay.SocialMedia.Controller;

import com.jay.SocialMedia.DTO.ChatMessageDTO;
import com.jay.SocialMedia.DTO.SendMessageRequest;
import com.jay.SocialMedia.Service.ChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/chats", "/chats"})
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{friendId}/messages")
    public List<ChatMessageDTO> getMessages(
            @PathVariable Long friendId,
            @RequestParam Long currentUserId
    ) {
        return chatService.getMessages(currentUserId, friendId);
    }

    @PostMapping("/{friendId}/messages")
    public ChatMessageDTO sendMessage(
            @PathVariable Long friendId,
            @RequestBody @Valid SendMessageRequest request
    ) {
        return chatService.sendMessage(friendId, request);
    }
}
